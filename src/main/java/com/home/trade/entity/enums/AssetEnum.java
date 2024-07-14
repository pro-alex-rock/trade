package com.home.trade.entity.enums;

public enum AssetEnum {
    BTC("BTC"),
    BNB("BNB"),
    ETH("ETH"),
    USDT("USDT"),
    USDC("USDC"),
    BUSD("BUSD");

    private final String label;

    AssetEnum(String label) {
        this.label = label;
    }

    public static boolean isAsset(String value) {
        if (value == null || value.equals("")) {
            return false;
        }
        for (AssetEnum en : values()) {
            if (value.equalsIgnoreCase(en.label)) {
                return true;
            }
        }
        return false;
    }
}
