package com.orb.interview.model.rate;

import com.orb.interview.controller.rate.GetRateRsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Service
public class RateService {
    //This is blocking so we rely on @Async to run it on separate thread
    //There seem to be new thingy WebClient which is part of reactive stack in Spring
    //But since we don't use it for now and I'm not familiar with Spring enough, we should be fine with blocking approach
    private final RestTemplate http;
    //Order of initialization for statics is important
    private static final Logger logger = LoggerFactory.getLogger(RateService.class);
    private static final String API_KEY = get_api_key();

    private static String get_api_key() {
        try {
            return new BufferedReader(new InputStreamReader(new ClassPathResource("secret.fixer").getInputStream())).readLine().trim();
        } catch (IOException err) {
            logger.error("Unable to load Fxier API key from resource secret.fixer");
            return "";
        }
    }

    public RateService() {
        this.http = new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(5)).build();
    }

    @Async
    public CompletableFuture<GetRateRsp> get_rate(String from, String to) throws InterruptedException {
        return CompletableFuture.completedFuture(new GetRateRsp(0.0));
    }
}
