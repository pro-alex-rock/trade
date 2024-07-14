package com.home.trade.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SymbolAccountInfo {
    private String symbol;
    private Integer initialMargin;
    private Integer maintMargin;
    private Double unrealizedProfit;
    private Integer positionInitialMargin;
    private Integer openOrderInitialMargin;
    private Integer leverage;
    private Boolean isolated;
    private Double entryPrice;
    private Integer maxNotional;
    private String positionSide;
    private String positionAmt;
    private String notional;
    private String isolatedWallet;
    private String updateTime;
    private String bidNotional;
    private String askNotional;
}
