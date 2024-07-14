package com.home.trade.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


/**
 * For subscribe to <symbol>@kline_<interval>
 */

@Data
@EqualsAndHashCode(callSuper=false)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class KlinePayload {

    private long timestamp;
    @JsonProperty("e")
    private String eventType;
    @JsonProperty("E")
    private long eventTime;
    @JsonProperty("s")
    private String symbol;
    @JsonProperty("k")
    private Candlestick candlestick;

    public double getHighPrice() {
        return Double.parseDouble(candlestick.getHighPrice());
    }

    public double getLowPrice() {
        return Double.parseDouble(candlestick.getLowPrice());
    }

    public double getOpenPrice() {
        return Double.parseDouble(candlestick.getOpenPrice());
    }

    public double getClosePrice() {
        return Double.parseDouble(candlestick.getClosePrice());
    }
}
