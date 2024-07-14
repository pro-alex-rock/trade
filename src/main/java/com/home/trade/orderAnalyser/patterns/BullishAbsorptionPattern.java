package com.home.trade.orderAnalyser.patterns;

import com.home.trade.entity.AnalyzerObserver;
import com.home.trade.entity.KlinePayload;
import com.home.trade.service.ProcessNewDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BullishAbsorptionPattern implements AnalyzerObserver {

    @Autowired
    private ProcessNewDataService processNewDataService;

    private boolean isBullishAbsorption;

    {
        processNewDataService.addObserver(this);
    }

    public boolean isBullishAbsorption() { //TODO ендпоінт до Акумулятора. Можливо Observer чи Spring Events (1)
        return isBullishAbsorption;
    }
    @Override
    public void reCalculate(KlinePayload klinePayload) {
        final String interval = klinePayload.getCandlestick().getInterval();
        List<KlinePayload> candles = processNewDataService.getCandlesListByInterval(interval);
        KlinePayload firstCandle = candles.get(candles.size() - 3);
        KlinePayload secondCandle = candles.get(candles.size() - 2);
        KlinePayload thirdCandle = candles.get(candles.size() - 1);
        identifyBullishAbsorption(firstCandle, secondCandle, thirdCandle);
    }

    private void identifyBullishAbsorption(KlinePayload firstCandle,
                                           KlinePayload secondCandle,
                                           KlinePayload thirdCandle) {
        boolean isFirstCandleBearish = isCandleBearish(firstCandle, secondCandle);
        boolean isBullishAbsorption = isBullishAbsorption(firstCandle, secondCandle, thirdCandle);
        isBullishAbsorption = isFirstCandleBearish && isBullishAbsorption;
    }

    private boolean isBullishAbsorption(KlinePayload firstCandle, KlinePayload secondCandle, KlinePayload thirdCandle) {
        double firstClosePrice = firstCandle.getClosePrice();
        double secondOpenPrice = secondCandle.getOpenPrice();
        double secondClosePrice = secondCandle.getClosePrice();
        double thirdOpenPrice = thirdCandle.getOpenPrice();
        double thirdClosePrice = thirdCandle.getClosePrice();

        double secondHighPrice = secondCandle.getHighPrice();
        double secondLowPrice = secondCandle.getLowPrice();
        double thirdHighPrice = thirdCandle.getHighPrice();
        double thirdLowPrice = thirdCandle.getLowPrice();

        boolean isSecondCandleBearish = secondClosePrice < secondOpenPrice;
        double secondDiffOpenToClose = secondOpenPrice - secondClosePrice;
        double thirdDiffOpenToClose = thirdClosePrice - thirdOpenPrice;
        boolean isAbsorptionBody = (thirdDiffOpenToClose / secondDiffOpenToClose > 2)
                && (secondClosePrice >= thirdOpenPrice)
                && (secondOpenPrice < thirdClosePrice);

        boolean isAbsorptionShadow = (secondHighPrice < thirdHighPrice) && (secondLowPrice > thirdLowPrice);
        return (isSecondCandleBearish && thirdDiffOpenToClose > 0)
                && isAbsorptionBody
                && isAbsorptionShadow
                && firstClosePrice < thirdClosePrice;
    }

    private boolean isCandleBearish(KlinePayload firstCandle, KlinePayload secondCandle) {
        double firstOpenPrice = firstCandle.getOpenPrice();
        double firstClosePrice = firstCandle.getClosePrice();
        double secondOpenPrice = secondCandle.getOpenPrice();
        double secondClosePrice = secondCandle.getClosePrice();
        return (firstClosePrice < firstOpenPrice)
                && (secondOpenPrice < firstClosePrice)
                && (secondClosePrice < firstClosePrice);
    }
}
