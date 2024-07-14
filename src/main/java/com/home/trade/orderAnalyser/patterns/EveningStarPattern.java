package com.home.trade.orderAnalyser.patterns;

import com.home.trade.entity.AnalyzerObserver;
import com.home.trade.entity.KlinePayload;
import com.home.trade.service.ProcessNewDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EveningStarPattern implements AnalyzerObserver {

    @Autowired
    private ProcessNewDataService processNewDataService;

    private boolean isEveningStar;
    private boolean morePower; //TODO якщо є розриви з двох сторін, якщо це ведмежа свічка

    {
        processNewDataService.addObserver(this);
    }

    public boolean isEveningStar() { //TODO ендпоінт до Акумулятора. Можливо Observer чи Spring Events (1)
        return isEveningStar;
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
        KlinePayload thirdCandle = candles.get(candles.size() - 1);
        identifyEveningStar(firstCandle, middleCandle, thirdCandle);
    }

    private void identifyEveningStar(KlinePayload firstCandle,
                                     KlinePayload middleCandle,
                                     KlinePayload thirdCandle) {
        boolean isMiddleIsEveningStar = isMiddleEveningStar(firstCandle, middleCandle, thirdCandle);
        boolean isPatternIsEveningStar = isPatternEveningStar(firstCandle, middleCandle, thirdCandle);
        isEveningStar = isMiddleIsEveningStar && isPatternIsEveningStar;
    }

    private boolean isPatternEveningStar(KlinePayload firstCandle,
                                         KlinePayload middleCandle,
                                         KlinePayload thirdCandle) {
        boolean isFirstCandleBullish = isFirstCandleBullish(firstCandle);
        boolean isLastCandleBearish = isLastCandleBearish(thirdCandle);
        boolean isSecondCandleLowerFirstAndLast = isSecondCandleHigherFirstAndLast(firstCandle, middleCandle, thirdCandle);
        return isFirstCandleBullish && isSecondCandleLowerFirstAndLast && isLastCandleBearish;
    }

    private boolean isSecondCandleHigherFirstAndLast(KlinePayload firstCandle,
                                                     KlinePayload middleCandle,
                                                     KlinePayload thirdCandle) {
        double firstClosePrice = firstCandle.getClosePrice();
        double lastOpenPrice = thirdCandle.getOpenPrice();
        double middleOpenPrice = middleCandle.getOpenPrice();
        double middleClosePrice = middleCandle.getClosePrice();

        if (morePower) {
            return (middleClosePrice >= firstClosePrice) && (middleClosePrice >= lastOpenPrice);
        } else {
            return (middleOpenPrice >= firstClosePrice) &&(middleOpenPrice >= lastOpenPrice);
        }
    }

    private boolean isLastCandleBearish(KlinePayload thirdCandle) {
        double lastOpenPrice = thirdCandle.getOpenPrice();
        double lastClosePrice = thirdCandle.getClosePrice();
        return lastClosePrice < lastOpenPrice;
    }

    private boolean isFirstCandleBullish(KlinePayload firstCandle) {
        double firstOpenPrice = firstCandle.getOpenPrice();
        double firstClosePrice = firstCandle.getClosePrice();
        return firstClosePrice > firstOpenPrice;
    }

    private boolean isMiddleEveningStar(KlinePayload firstCandle, KlinePayload middleCandle, KlinePayload thirdCandle) {
        double middleOpenPrice = middleCandle.getOpenPrice();
        double middleClosePrice = middleCandle.getClosePrice();
        double middleHighPrice = middleCandle.getHighPrice();
        double middleLowPrice = middleCandle.getLowPrice();

        double middleDiffOpenToClose = middleClosePrice - middleOpenPrice;
        double middleDiffHighToLow = middleHighPrice - middleLowPrice;

        double firstOpenPrice = firstCandle.getOpenPrice();
        double firstClosePrice = firstCandle.getClosePrice();
        double thirdOpenPrice = thirdCandle.getOpenPrice();
        double thirdClosePrice = thirdCandle.getClosePrice();

        double firstDiffOpenToClose = firstClosePrice - firstOpenPrice;
        double thirdDiffOpenToClose = thirdOpenPrice - thirdClosePrice;

        boolean isMiddleBodyLess = (middleDiffOpenToClose < firstDiffOpenToClose/2)
                && (middleDiffOpenToClose < thirdDiffOpenToClose/2);
        boolean isMiddleHasShadow = Math.abs(middleDiffHighToLow) > Math.abs(middleDiffOpenToClose);

        boolean isLeftGap = firstClosePrice < middleOpenPrice && firstClosePrice < middleClosePrice;
        boolean isRightGap = thirdOpenPrice < middleOpenPrice && thirdOpenPrice < middleClosePrice;
        morePower = (middleOpenPrice > middleClosePrice)
                && isLeftGap
                && isRightGap;

        return isMiddleBodyLess && isMiddleHasShadow;
    }
}
