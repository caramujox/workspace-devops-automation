package com.nu.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
@JsonIgnoreProperties(ignoreUnknown = true)
public record Operation (String operation,
                         @JsonProperty("unit-cost") BigDecimal unitCost,
                         Integer quantity) {

}
