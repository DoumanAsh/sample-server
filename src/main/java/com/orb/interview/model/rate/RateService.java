package com.orb.interview.model.rate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.orb.interview.controller.rate.GetRateRsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Service
public class RateService {
    private static final long OLD_INTERVAL_MS = 90 * 60 * 1000;
    //This is blocking so we rely on @Async to run it on separate thread
    //There seem to be new thingy WebClient which is part of reactive stack in Spring
    //But since we don't use it and I'm not familiar with Spring enough, we should be fine with blocking approach, which use thread pool
    @Autowired
    private RestTemplate http;
    private Cache cache = new Cache();
    //Order of initialization for statics is important
    private static final Logger logger = LoggerFactory.getLogger(RateService.class);
    private static final String API_KEY = get_api_key();

    //Cache is always initialized and for simplicity sake date is 1970 UTC
    //so that it would be invalidated first time always
    static class Cache {
        public Date last_req = new Date(0);
        public String base;
        public FixerRates rates;

        public Cache() {}
    }

    private static String get_api_key() {
        try {
            return new BufferedReader(new InputStreamReader(new ClassPathResource("secret.fixer").getInputStream())).readLine().trim();
        } catch (IOException err) {
            logger.error("Unable to load Fxier API key from resource secret.fixer");
            return "";
        }
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(5)).build();
    }

    public RateService() {}

    public void purge_cache() {
        this.cache.last_req = new Date(0);
    }

    private synchronized void update_cache() throws RestClientException {
        Date now = new Date(System.currentTimeMillis());
        long diff_ms = now.getTime() - this.cache.last_req.getTime();

        if (diff_ms < OLD_INTERVAL_MS) {
            return;
        }

        logger.debug("Update currency cache");
        String url = String.format("http://data.fixer.io/api/latest?access_key=%s&symbols=USD,EUR,JPY", API_KEY);
        FixerRsp rsp = http.getForObject(url, FixerRsp.class);

        if (rsp == null || !rsp.success) {
            logger.error("Fixer failed to return response");
            throw new RuntimeException();
        }

        this.cache.last_req = now;
        this.cache.base = rsp.base;
        this.cache.rates = rsp.rates;
    }

    private GetRateRsp request_rate(String from, String to) throws RestClientException, RuntimeException {
        update_cache();

        if (from.equals(this.cache.base)) {
            return new GetRateRsp(this.cache.rates.get_rate_by_name(to));
        } else if (to.equals(this.cache.base)) {
            return new GetRateRsp(1.0 / this.cache.rates.get_rate_by_name(from));
        } else {
            double from_rate = this.cache.rates.get_rate_by_name(from);
            double to_rate = this.cache.rates.get_rate_by_name(to);
            return new GetRateRsp(to_rate / from_rate);
        }
    }

    @Async
    public CompletableFuture<GetRateRsp> get_rate(String from, String to) throws InterruptedException, RestClientException {
        return CompletableFuture.completedFuture(request_rate(from, to));
    }
}
