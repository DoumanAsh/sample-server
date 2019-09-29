package com.orb.interview.controller.rate;

import com.orb.interview.model.rate.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/rates")
public class RateController {
    @Autowired
    private RateService rateService;

    @GetMapping(produces = "application/json")
    public GetRateRsp get_rates(@Valid GetRate params) throws InterruptedException, ExecutionException {
        return rateService.get_rate(params.getFrom(), params.getTo()).get();
    }
}
