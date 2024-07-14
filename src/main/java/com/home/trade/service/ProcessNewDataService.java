package com.home.trade.service;

import com.home.trade.entity.AnalyzerObserver;
import com.home.trade.entity.NewDataObserver;
import com.home.trade.entity.KlinePayload;
import com.home.trade.orderAnalyser.EmaAnalyser;
import com.home.trade.service.soa.SoaMongoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProcessNewDataService implements NewDataObserver {

    /*@Autowired
    private ApplicationEventPublisher applicationEventPublisher;*/
    @Autowired
    private SoaMongoManager soaMongoManager;

    private List<AnalyzerObserver> analyzerObservers = new ArrayList<>();
    private Map<String, List<KlinePayload>> collectionsMap = new HashMap<>();
    private List<KlinePayload> candles_1h = new ArrayList<>();
    private List<KlinePayload> candles_4h = new ArrayList<>();
    private List<KlinePayload> candles_1d = new ArrayList<>();
    private List<KlinePayload> candles_1w = new ArrayList<>();
    private List<KlinePayload> candles_1M = new ArrayList<>();

    {
        collectionsMap.put("btcusdt_1h", candles_1h);
        collectionsMap.put("btcusdt_4h", candles_4h);
        collectionsMap.put("btcusdt_1d", candles_1d);
        collectionsMap.put("btcusdt_1w", candles_1w);
        collectionsMap.put("btcusdt_1M", candles_1M);
        //analyzerObservers.add(emaAnalyser);
    }

    public void retrieveDataFromDb() {
        collectionsMap.forEach((collectionName, list) -> {
            List<KlinePayload> dataFromDb = (List<KlinePayload>) soaMongoManager.findAll(KlinePayload.class, collectionName);
            list.addAll(dataFromDb);
        });
    }

    @Override
    public void update(KlinePayload klinePayload) {
        System.out.println("ProcessDataService -->" + klinePayload);

        final String interval = klinePayload.getCandlestick().getInterval();

        collectionsMap.entrySet().stream()
                .filter(entry -> entry.getKey().endsWith(interval))
                .findFirst()
                .map(entry -> entry.getValue().add(klinePayload));
        analyzerObservers.forEach(analyzerObserver -> analyzerObserver.reCalculate(klinePayload));
        //applicationEventPublisher.publishEvent(klinePayload);
    }
    /*
    продумати логіку, як індикатор буде відстежувати поповнення List<Data>
    і брати онлайн звідти дані.
    можливо гетери тут не потрібні, а написати щось типу Observer чи Spring Events, або напряму
    записувати в List<Data> індикатора.
     */

    public void addObserver(AnalyzerObserver observer) {
        analyzerObservers.add(observer);
    }

    public List<KlinePayload> getCandlesListByInterval(String interval) {
        return switch (interval) {
            case "1h" -> collectionsMap.get("btcusdt_1h");
            case "4h" -> collectionsMap.get("btcusdt_4h");
            case "1d" -> collectionsMap.get("btcusdt_1d");
            case "1w" -> collectionsMap.get("btcusdt_1w");
            case "1M" -> collectionsMap.get("btcusdt_1M");
            default -> throw new RuntimeException("Invalid interval!");
        };
    }

    public List<KlinePayload> getCandlesOneHour() {
        return candles_1h;
    }

    public List<KlinePayload> getCandlesFourHours() {
        return candles_4h;
    }

    public List<KlinePayload> getCandlesOneDay() {
        return candles_1d;
    }

    public List<KlinePayload> getCandlesOneWeek() {
        return candles_1w;
    }

    public List<KlinePayload> getCandlesOneMonth() {
        return candles_1M;
    }

    public Map<String, List<KlinePayload>> getCollectionsMap() {
        return collectionsMap;
    }
}
