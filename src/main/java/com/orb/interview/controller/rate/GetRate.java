package com.orb.interview.controller.rate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class GetRate {
    private String from;
    private String to;

    GetRate() {}

    @NotBlank(message = "{com.orb.interview.rate.get.FromMissing}")
    @Pattern(regexp = "JPY|EUR|USD", message = "{com.orb.interview.rate.get.InvalidCode}")
    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @NotBlank(message = "{com.orb.interview.rate.get.ToMissing}")
    @Pattern(regexp = "JPY|EUR|USD", message = "{com.orb.interview.rate.get.InvalidCode}")
    public String getTo() {
        return this.to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
