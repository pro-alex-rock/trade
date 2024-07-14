package com.home.trade.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class Asset {
    @JsonProperty("id")
    private String id;
    @JsonProperty("assetCode")
    private String assetCode;
    @JsonProperty("assetName")
    private String assetName;
    @JsonProperty("unit")
    private String unit;
    @JsonProperty("transactionFee")
    private String transactionFee;
    @JsonProperty("commissionRate")
    private String commissionRate;
    @JsonProperty("freeAuditWithdrawAmt")
    private String freeAuditWithdrawAmount;
    @JsonProperty("freeUserChargeAmount")
    private String freeUserChargeAmount;
    @JsonProperty("minProductWithdraw")
    private String minProductWithdraw;
    @JsonProperty("withdrawIntegerMultiple")
    private String withdrawIntegerMultiple;
    @JsonProperty("confirmTimes")
    private long confirmTimes;
    @JsonProperty("enableWithdraw")
    private boolean enableWithdraw;
    @JsonProperty("isLegalMoney")
    private boolean isLegalMoney;
}
