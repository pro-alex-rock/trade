package com.home.trade.orderAnalyser.patterns;

import com.home.trade.entity.AnalyzerObserver;
import com.home.trade.entity.KlinePayload;
import com.home.trade.service.ProcessNewDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HummerPattern implements AnalyzerObserver {

    @Autowired
    private ProcessNewDataService processNewDataService;

    private boolean isHummer;
    private boolean morePower;

    {
        processNewDataService.addObserver(this);
    }

    public boolean isHummer() { //TODO ендпоінт до Акумулятора. Можливо Observer чи Spring Events (1)
        return isHummer;
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
        identifyHummer(firstCandle, middleCandle, lastCandle);
    }

    private void identifyHummer(KlinePayload firstCandle, KlinePayload middleCandle, KlinePayload lastCandle) {
        boolean isMiddleIsHummer = isMiddleHummer(middleCandle);
        boolean isPatternIsHummer = isPatternHummer(firstCandle, middleCandle, lastCandle);
        isHummer = isMiddleIsHummer && isPatternIsHummer;
    }

    private boolean isPatternHummer(KlinePayload firstCandle, KlinePayload middleCandle, KlinePayload lastCandle) {
        boolean isFirstCandleBearish = isFirstCandleBearish(firstCandle);
        boolean isLastCandleBullish = isLastCandleBullish(lastCandle);
        boolean isSecondCandleLowerFirstAndLast = isSecondCandleLowerFirstAndLast(firstCandle, middleCandle, lastCandle);
        if(isFirstCandleBearish && isLastCandleBullish && isSecondCandleLowerFirstAndLast) {
            return true;
        }
        return false;
    }

    /*
    TODO прописати варіант Pin-Bar - хамер в тілі першої свічки
     */
    private boolean isSecondCandleLowerFirstAndLast(KlinePayload firstCandle, KlinePayload middleCandle, KlinePayload lastCandle) {
        double firstClosePrice = firstCandle.getClosePrice();
        double lastOpenPrice = lastCandle.getOpenPrice();
        double middleOpenPrice = middleCandle.getOpenPrice();
        double middleClosePrice = middleCandle.getClosePrice();

        if (morePower) {
            return (middleClosePrice < firstClosePrice) && (middleClosePrice < lastOpenPrice);
        } else {
            return (middleOpenPrice < firstClosePrice) &&(middleOpenPrice < lastOpenPrice);
        }
    }

    private boolean isLastCandleBullish(KlinePayload lastCandle) {
        double lastOpenPrice = lastCandle.getOpenPrice();
        double lastClosePrice = lastCandle.getClosePrice();
        return lastClosePrice > lastOpenPrice;
    }

    private boolean isFirstCandleBearish(KlinePayload firstCandle) {
        double firstOpenPrice = firstCandle.getOpenPrice();
        double firstClosePrice = firstCandle.getClosePrice();
        return firstClosePrice < firstOpenPrice;
    }

    private boolean isMiddleHummer(KlinePayload middleCandle) {
        double openPrice = middleCandle.getOpenPrice();
        double closePrice = middleCandle.getClosePrice();
        double highPrice = middleCandle.getHighPrice();
        double lowPrice = middleCandle.getLowPrice();

        double diffOpenToClose = openPrice - closePrice;
        double diffHighToLow = highPrice - lowPrice;
        morePower = diffOpenToClose < 0;

        boolean isBodyLessThanQuarter = Math.abs(diffOpenToClose) < diffHighToLow/4;
        boolean isBodyAbove = highPrice - (morePower ? closePrice : openPrice) < Math.abs(diffOpenToClose)/2;
        return isBodyLessThanQuarter && isBodyAbove;
    }


}
