package com.home.trade.orderAnalyser;

import com.home.trade.entity.AnalyzerObserver;
import com.home.trade.entity.KlinePayload;
import com.home.trade.service.ProcessNewDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class VolumeAnalyzer implements AnalyzerObserver {

    @Autowired
    private ProcessNewDataService processNewDataService;
    private Map<String, List<KlinePayload>> collectionsMap = processNewDataService.getCollectionsMap();

    private List<Double> volume1h = new ArrayList<>();
    private List<Double> volume4h = new ArrayList<>();
    private List<Double> volume1d = new ArrayList<>();
    private List<Double> volume1w = new ArrayList<>();
    private List<Double> volume1M = new ArrayList<>();

    {
        processNewDataService.addObserver(this);
    }
    @Override
    public void reCalculate(KlinePayload klinePayload) {
        calculate1h();
        calculate4h();
        calculate1d();
        calculate1w();
        calculate1M();
    }

    private List<Double> convertToVolumes(List<KlinePayload> klinePayloadList) {
        return klinePayloadList.stream()
                .map(klinePayload -> klinePayload.getCandlestick().getBaseAssetVolume())
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }

    private void calculate1h() {
        convertToVolumes(collectionsMap.get("btcusdt_1h"));
    }

    private void calculate4h() {
        convertToVolumes(collectionsMap.get("btcusdt_4h"));
    }

    private void calculate1d() {
        convertToVolumes(collectionsMap.get("btcusdt_1d"));
    }

    private void calculate1w() {
        convertToVolumes(collectionsMap.get("btcusdt_1w"));
    }

    private void calculate1M() {
        convertToVolumes(collectionsMap.get("btcusdt_1M"));
    }

    public List<Double> getVolumeByInterval(String interval) {
        return switch (interval) {
            case "1h" -> volume1h;
            case "4h" -> volume4h;
            case "1d" -> volume1d;
            case "1w" -> volume1w;
            case "1M" -> volume1M;
            default -> throw new RuntimeException("Invalid interval!");
        };
    }

    public List<Double> getVolume1h() {
        return volume1h;
    }

    public List<Double> getVolume4h() {
        return volume4h;
    }

    public List<Double> getVolume1d() {
        return volume1d;
    }

    public List<Double> getVolume1w() {
        return volume1w;
    }

    public List<Double> getVolume1M() {
        return volume1M;
    }
}
