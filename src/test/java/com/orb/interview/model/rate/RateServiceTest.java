package com.orb.interview.model.rate;

import com.orb.interview.controller.rate.GetRateRsp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = RateService.class)
public class RateServiceTest {
    @MockBean
    private RestTemplate http;
    @Autowired
    private RateService rateService;

    private final FixerRsp DATA;

    public RateServiceTest() {
        DATA = new FixerRsp();
        DATA.success = true;
        DATA.base = "EUR";
        DATA.rates = new FixerRates();
        DATA.rates.eur = 1.0;
        DATA.rates.usd = 1.094103;
        DATA.rates.jpy = 118.092091;
    }

    @After
    public void tearDown() {
        rateService.purge_cache();
    }

    @Test
    public void should_handle_straight_rate_get() throws InterruptedException, ExecutionException {
        when(http.getForObject((String)notNull(), notNull())).thenReturn(DATA);

        GetRateRsp result = rateService.get_rate("EUR", "JPY").get();
        assertEquals(result.rate, DATA.rates.jpy, 0.0);
    }

    @Test
    public void should_handle_reverse_rate_get() throws InterruptedException, ExecutionException {
        when(http.getForObject((String)notNull(), notNull())).thenReturn(DATA);

        GetRateRsp result = rateService.get_rate("JPY", "EUR").get();
        assertEquals(result.rate, 1.0 / DATA.rates.jpy, 0.0);
    }

    @Test
    public void should_handle_third_rate_get() throws InterruptedException, ExecutionException {
        when(http.getForObject((String)notNull(), notNull())).thenReturn(DATA);

        GetRateRsp result = rateService.get_rate("JPY", "USD").get();
        assertEquals(result.rate, DATA.rates.usd / DATA.rates.jpy, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void should_throw_on_api_failure() throws InterruptedException, ExecutionException {
        FixerRsp api_data = new FixerRsp();
        api_data.success = false;
        api_data.base = "EUR";
        api_data.rates = new FixerRates();
        api_data.rates.eur = 1.0;
        api_data.rates.usd = 1.094103;
        api_data.rates.jpy = 118.092091;

        when(http.getForObject((String)notNull(), notNull())).thenReturn(api_data);

        rateService.get_rate("EUR", "JPY");
    }
}
