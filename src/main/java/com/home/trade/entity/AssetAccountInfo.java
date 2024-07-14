package com.home.trade.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AssetAccountInfo {
    private String asset;
    private Double walletBalance; //precision 8
    private Double unrealizedProfit;
    private Double marginBalance;
    private Double maintMargin;
    private Double initialMargin;
    private Double positionInitialMargin;
    private Double openOrderInitialMargin;
    private Double maxWithdrawAmount;
    private Double crossWalletBalance;
    private Double crossUnPnl;
    private Double availableBalance;
    private Boolean marginAvailable;
    private Long updateTime;
}
