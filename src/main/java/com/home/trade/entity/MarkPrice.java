package com.home.trade.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MarkPrice {
    private String symbol;
    private Double markPrice;
    private Double indexPrice;
    private Double estimatedSettlePrice;
    private Double lastFundingRate;
    private Double interestRate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Long nextFundingTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Long time;
}
