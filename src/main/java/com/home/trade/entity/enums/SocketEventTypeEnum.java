package com.home.trade.entity.enums;

public enum SocketEventTypeEnum {

    DEPTH("@depth"),      //bnbbtc@depth@500ms
    KLINE("@kline");     //<symbol>@kline_<interval>

    private final String label;

    SocketEventTypeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
