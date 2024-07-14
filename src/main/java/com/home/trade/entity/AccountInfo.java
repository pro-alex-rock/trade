package com.home.trade.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class AccountInfo {
    private Integer feeTier;
    private Boolean canTrade;
    private Boolean canDeposit;
    private Boolean canWithdraw;
    private Long updateTime;
    private Boolean multiAssetsMargin;
    private Double totalInitialMargin;
    private Double totalMaintMargin;
    private Double totalWalletBalance;
    private Double totalUnrealizedProfit;
    private Double totalMarginBalance;
    private Double totalPositionInitialMargin;
    private Double totalOpenOrderInitialMargin;
    private Double totalCrossWalletBalance;
    private Double totalCrossUnPnl;
    private Double availableBalance;
    private Double maxWithdrawAmount;
    @JsonProperty("assets")
    private List<AssetAccountInfo> assetAccountInfos;
    @JsonProperty("positions")
    private List<SymbolAccountInfo> symbolAccountInfos;
}
