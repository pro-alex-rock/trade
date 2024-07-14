package com.home.trade.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ChangesPrice24Hr {
    private String symbol;
    private Double priceChange;
    private Double priceChangePercent;
    private Double weightedAvgPrice;
    private Double lastPrice;
    private Double lastQty;
    private Double openPrice;
    private Double highPrice;
    private Double lowPrice;
    private Double volume;
    private Double quoteVolume;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Long openTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Long closeTime;
    private Integer firstId;
    private Integer lastId;
    private Integer count;
}
