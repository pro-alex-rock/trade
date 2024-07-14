package com.home.trade.orderAnalyser;

import com.home.trade.entity.AnalyzerObserver;
import com.home.trade.entity.KlinePayload;
import com.home.trade.indicators.indicatorEMA.ExponentialMovingAverage;
import com.home.trade.service.ProcessNewDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EmaAnalyser implements AnalyzerObserver {

    private Boolean isUp;
    private Boolean isFlat;

    @Autowired
    private ProcessNewDataService processNewDataService;
    private final ExponentialMovingAverage movingAverage;
    private final TrendAnalyser trendAnalyser = new TrendAnalyser();
    private final EmaOneHour emaOneHourInstance = new EmaOneHour();
    private final EmaOneDay emaOneDayInstance = new EmaOneDay();
    private Map<String, List<KlinePayload>> collectionsMap = processNewDataService.getCollectionsMap();

    @Autowired
    public EmaAnalyser(ExponentialMovingAverage movingAverage) {
        this.movingAverage = movingAverage;
        processNewDataService.addObserver(this);
    }

    /**
     * Launches the first calculation of the indicator immediately after creating beans and
     * finish of work runners annotated @Order
     * @param event
     */
    @EventListener
    public void onContextRefreshed(ContextRefreshedEvent event) {
        calculateOnStart();
    }


    /**
     * Previously, the new data that arrived was already recorded in the collectionsMap.
     * Now in this method, depending on the interval, the method by which Ema recalculates
     * the new Average is activated.
     * @param klinePayload - new data that arrived.
     */
    @Override
    public void reCalculate(KlinePayload klinePayload) {
        final String interval = klinePayload.getCandlestick().getInterval();

        if(interval.equalsIgnoreCase("1h")) {
            calculateOneHour();
        } else if(interval.equalsIgnoreCase("1d")) {
            calculateOneDay();
        } else {
            throw new RuntimeException("Unknown interval!");
        }
    }

   /* public void analyzeDirectionOfTrend() {
        // Переконуємося, що список містить хоча б два значення для аналізу
        if (emaSevenDays.size() < 2) {
            throw new IllegalArgumentException("List should contain at least two values for analysis");
        }

        double differenceSum = 0;
        for (int i = 1; i < emaSevenDays.size(); i++) {
            differenceSum += emaSevenDays.get(i) - emaSevenDays.get(i - 1);
        }

        // Тут ми використовуємо невелике порогове значення для визначення "плоскості". Це потрібно,
        // щоб врахувати можливу похибку або невелике коливання у ваших даних.
        double threshold = 0.0001;

        if (Math.abs(differenceSum) < threshold) {
            isFlat = true;
            isUp = null;  // можна встановити в null, якщо немає чіткого напрямку
        } else {
            isUp = differenceSum > 0;
            isFlat = false;
        }
    }*/


    /**
     * Calculate first points after program start.
     */
    private void calculateOnStart() {
        calculateOneHour();
        calculateOneDay();
    }

    private void calculateOneDay() {
        List<KlinePayload> btcusdt_1D = collectionsMap.get("btcusdt_1D");
        List<Double> dataClose = btcusdt_1D.stream()
                .map(klinePayload -> klinePayload.getCandlestick().getClosePrice())
                .map(Double::parseDouble)
                .collect(Collectors.toList());
        List<Double> emaResults = movingAverage.calculateEma(dataClose, 50);
        emaOneDayInstance.getShortEma().addAll(emaResults);
        emaResults = movingAverage.calculateEma(dataClose, 100);
        emaOneDayInstance.getMediumEma().addAll(emaResults);
        emaResults = movingAverage.calculateEma(dataClose, 200);
        emaOneDayInstance.getLongEma().addAll(emaResults);
    }

    private void calculateOneHour() {
        List<KlinePayload> btcusdt_1h = collectionsMap.get("btcusdt_1h");
        List<Double> dataClose = btcusdt_1h.stream()
                .map(klinePayload -> klinePayload.getCandlestick().getClosePrice())
                .map(Double::parseDouble)
                .collect(Collectors.toList());
        List<Double> emaResults = movingAverage.calculateEma(dataClose, 7);
        emaOneHourInstance.getShortEma().addAll(emaResults);
        emaResults = movingAverage.calculateEma(dataClose, 25);
        emaOneHourInstance.getMediumEma().addAll(emaResults);
        emaResults = movingAverage.calculateEma(dataClose, 50);
        emaOneHourInstance.getLongEma().addAll(emaResults);
    }

    /*@Override
    public void onApplicationEvent(KlinePayload event) {
        final String interval = event.getCandlestick().getInterval();
        if(interval.equalsIgnoreCase("1h")) {
            calculateOneHour();
        } else if(interval.equalsIgnoreCase("1d")) {
            calculateOneDay();
        } else {
            throw new RuntimeException("Unknown interval!");
        }
    }*/

    interface EmaData {
        List<Double> getShortEma();
        List<Double> getMediumEma();
        List<Double> getLongEma();
    }

    @lombok.Data
    static class EmaOneHour implements EmaData {
        private List<Double> shortEma = new ArrayList<>();  // 7 days
        private List<Double> mediumEma = new ArrayList<>(); // 25 days
        private List<Double> longEma = new ArrayList<>();   // 50 days
    }

    @lombok.Data
    static class EmaOneDay implements EmaData {
        private List<Double> shortEma = new ArrayList<>();  // 50 days
        private List<Double> mediumEma = new ArrayList<>(); // 100 days
        private List<Double> longEma = new ArrayList<>();   // 200 days
    }

    public void addToEmaOneHourSevenDays(Double value) {
        emaOneHourInstance.getShortEma().add(value);
    }

    public void addToEmaOneHourTwentyFiveDays(Double value) {
        emaOneHourInstance.getMediumEma().add(value);
    }

    public void addToEmaOneHourFiftyDays(Double value) {
        emaOneHourInstance.getLongEma().add(value);
    }

    // Methods to operate on EmaOneDay
    public void addToEmaOneDayFiftyDays(Double value) {
        emaOneDayInstance.getShortEma().add(value);
    }

    public void addToEmaOneDayOneHundredDays(Double value) {
        emaOneDayInstance.getMediumEma().add(value);
    }

    public void addToEmaOneDayTwoHundredDays(Double value) {
        emaOneDayInstance.getLongEma().add(value);
    }
}
