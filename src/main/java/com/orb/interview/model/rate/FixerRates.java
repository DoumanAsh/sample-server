package com.orb.interview.model.rate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FixerRates {
    @JsonProperty("EUR")
    public double eur;
    @JsonProperty("JPY")
    public double jpy;
    @JsonProperty("USD")
    public double usd;

    double get_rate_by_name(String name) throws RuntimeException {
        switch (name) {
            case "EUR": return this.eur;
            case "JPY": return this.jpy;
            case "USD": return this.usd;
            default: throw new RuntimeException(String.format("Unknown rate=%s"));
        }
    }

    public FixerRates() { }
}
