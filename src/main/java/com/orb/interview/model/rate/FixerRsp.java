package com.orb.interview.model.rate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FixerRsp {
    @JsonProperty("success")
    public Boolean success;
    @JsonProperty("base")
    public String base;
    @JsonProperty("rates")
    public FixerRates rates;

    public FixerRsp() { }
}

