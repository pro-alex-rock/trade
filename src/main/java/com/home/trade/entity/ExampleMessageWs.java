package com.home.trade.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class ExampleMessageWs {
    private String json;
}
