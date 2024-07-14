package com.home.trade.orderAnalyser.patterns;

import com.home.trade.entity.AnalyzerObserver;
import com.home.trade.entity.KlinePayload;
import com.home.trade.service.ProcessNewDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FallingStarPattern implements AnalyzerObserver {

    @Autowired
    private ProcessNewDataService processNewDataService;

    private boolean isFallingStar;
    private boolean morePower;

    {
        processNewDataService.addObserver(this);
    }

    public boolean isFallingStar() { //TODO ендпоінт до Акумулятора. Можливо Observer чи Spring Events (1)
        return isFallingStar;
    }

    public boolean isMorePower() { //TODO ендпоінт до Акумулятора. Можливо Observer чи Spring Events (2)
        return morePower;
    }
    @Override
    public void reCalculate(KlinePayload klinePayload) {
        final String interval = klinePayload.getCandlestick().getInterval();
        List<KlinePayload> candles = processNewDataService.getCandlesListByInterval(interval);
        KlinePayload firstCandle = candles.get(candles.size() - 3);
        KlinePayload middleCandle = candles.get(candles.size() - 2);
        KlinePayload lastCandle = candles.get(candles.size() - 1);
        identifyFallingStar(firstCandle, middleCandle, lastCandle);
    }

    private void identifyFallingStar(KlinePayload firstCandle, KlinePayload middleCandle, KlinePayload lastCandle) {
        boolean isMiddleIsFallingStar = isMiddleFallingStar(middleCandle);
        boolean isPatternIsFallingStar = isPatternFallingStar(firstCandle, middleCandle, lastCandle);
        isFallingStar = isMiddleIsFallingStar && isPatternIsFallingStar;
    }

    private boolean isPatternFallingStar(KlinePayload firstCandle, KlinePayload middleCandle, KlinePayload lastCandle) {
        boolean isFirstCandleBullish = isFirstCandleBullish(firstCandle);
        boolean isLastCandleBearish = isLastCandleBearish(lastCandle);
        boolean isSecondCandleLowerFirstAndLast = isSecondCandleHigherFirstAndLast(firstCandle, middleCandle, lastCandle);
        return isFirstCandleBullish && isLastCandleBearish && isSecondCandleLowerFirstAndLast;
    }

    /*
    TODO прописати варіант Pin-Bar - зірка в тілі першої свічки
     */
    private boolean isSecondCandleHigherFirstAndLast(KlinePayload firstCandle, KlinePayload middleCandle, KlinePayload lastCandle) {
        double firstClosePrice = firstCandle.getClosePrice();
        double lastOpenPrice = lastCandle.getOpenPrice();
        double middleOpenPrice = middleCandle.getOpenPrice();
        double middleClosePrice = middleCandle.getClosePrice();

        if (morePower) {
            return (middleClosePrice > firstClosePrice) && (middleClosePrice < lastOpenPrice);
        } else {
            return (middleOpenPrice > firstClosePrice) &&(middleOpenPrice > lastOpenPrice);
        }
    }

    private boolean isLastCandleBearish(KlinePayload lastCandle) {
        double lastOpenPrice = lastCandle.getOpenPrice();
        double lastClosePrice = lastCandle.getClosePrice();
        return lastClosePrice < lastOpenPrice;
    }

    private boolean isFirstCandleBullish(KlinePayload firstCandle) {
        double firstOpenPrice = firstCandle.getOpenPrice();
        double firstClosePrice = firstCandle.getClosePrice();
        return firstClosePrice > firstOpenPrice;
    }

    private boolean isMiddleFallingStar(KlinePayload middleCandle) {
        double openPrice = middleCandle.getOpenPrice();
        double closePrice = middleCandle.getClosePrice();
        double highPrice = middleCandle.getHighPrice();
        double lowPrice = middleCandle.getLowPrice();

        double diffOpenToClose = openPrice - closePrice;
        double diffHighToLow = highPrice - lowPrice;
        morePower = diffOpenToClose > 0;

        boolean isBodyLessThanQuarter = Math.abs(diffOpenToClose) < diffHighToLow/4;
        boolean isBodyDown = (morePower ? closePrice : openPrice) - lowPrice < Math.abs(diffOpenToClose)/2;
        return isBodyLessThanQuarter && isBodyDown;
    }
}
