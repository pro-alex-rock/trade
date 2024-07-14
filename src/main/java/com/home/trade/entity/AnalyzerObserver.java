package com.home.trade.entity;

import java.util.List;
import java.util.Map;

public interface AnalyzerObserver {
    void reCalculate(KlinePayload klinePayload);
    //void setCollectionsMap(Map<String, List<KlinePayload>> collectionsMap);
}
