package com.home.trade.entity.enums;

public enum UriEnum {
    KLINES("/fapi/v1/klines"),                  //all history by symbol
    TIME("/fapi/v1/time"),
    PRICE_24_HOURS("/fapi/v1/ticker/24hr"),
    MARK_PRICE("/fapi/v1/premiumIndex"),
    PRICE("/fapi/v1/ticker/price"),
    ORDER_BOOK("/fapi/v1/depth"),
    ACCOUNT("/fapi/v2/account"),
    BALANCE("/fapi/v2/balance");

    private final String label;

    UriEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public static boolean isUri(String value) {
        if (value == null || value.equals("")) {
            return false;
        }
        for (UriEnum en : values()) {
            if (value.equalsIgnoreCase(en.label)) {
                return true;
            }
        }
        return false;
    }
}
