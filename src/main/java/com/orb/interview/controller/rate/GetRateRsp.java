package com.orb.interview.controller.rate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetRateRsp {
    @JsonProperty("rate")
    public double rate;

    public GetRateRsp(double rate) {
        this.rate = rate;
    }
}
