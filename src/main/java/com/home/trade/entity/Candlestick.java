package com.home.trade.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

/**
 * for KlinePayload, JsonProperty = 'k'
 */

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Candlestick {
    /*private long timestamp;
    private Long openTime;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Double volume;
    private Long closeTime;
    private Double quoteAssetVolume;
    private Integer numberOfTrades;
    private Double takerBuyBaseAssetVolume;
    private Double takerBuyQuoteAssetVolume;
    private Double ignore;*/

    private long timestamp;
    @JsonProperty("t")
    private long klineStartTime;
    @JsonProperty("T")
    private long klineCloseTime;
    @JsonProperty("s")
    private String symbol;
    @JsonProperty("i")
    private String interval;
    @JsonProperty("f")
    private long firstTradeID;
    @JsonProperty("L")
    private long lastTradeID;
    @JsonProperty("o")
    private String openPrice;
    @JsonProperty("h")
    private String highPrice;
    @JsonProperty("l")
    private String lowPrice;
    @JsonProperty("c")
    private String closePrice;
    @JsonProperty("v")
    private String baseAssetVolume;
    @JsonProperty("n")
    private long numberOfTrades;
    @JsonProperty("x")
    private Boolean isThisKlineClosed;
    @JsonProperty("q")
    private String quoteAssetVolume;
    @JsonProperty("V")
    private String takerBuyBaseAssetVolume;
    @JsonProperty("Q")
    private String takerBuyQuoteAssetVolume;
    @JsonProperty("B")
    private String ignore;

    /*private long timestamp;
    private BigDecimal openPrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal closePrice;
    private BigDecimal volume;
    private long closeTime;
    private BigDecimal quoteAssetVolume;
    private int numberOfTrades;
    private BigDecimal takerBuyBaseAssetVolume;
    private BigDecimal takerBuyQuoteAssetVolume;
    private String ignore;*/
}
