package com.orb.interview.controller.rate;

import com.orb.interview.model.rate.RateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = RateController.class)
public class RateControllerTest {
    @Autowired
    private RateController rateController;
    @MockBean
    private RateService rateService;

    static final GetRateRsp EXPECTED_RESULT = new GetRateRsp(0.1);

    @Test
    public void should_invoke_rate_service() throws Exception {
        GetRate params = new GetRate();
        params.setFrom("JPY");
        params.setTo("USD");

        when(rateService.get_rate("JPY", "USD")).thenReturn(CompletableFuture.completedFuture(EXPECTED_RESULT));

        GetRateRsp result = rateController.get_rates(params);
        assertEquals(result, EXPECTED_RESULT);
    }
}
