package com.home.trade.indicators.indicatorMA;

import com.home.trade.indicators.Indicator;
import com.home.trade.service.soa.SoaMongoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SimpleMovingAverage implements Indicator {

    @Autowired
    private SoaMongoManager soaMongoManager;

    //private Map<String, List<Data>> collectionsMap = new HashMap<>();

/*
переписати в окремий сервіс. який буде впорядковувати записи в один лист і
в такому порядку слати до Kafka.
 */
    /*private List<Data> candles_1h = new ArrayList<>();
    private List<Data> candles_4h = new ArrayList<>();
    private List<Data> candles_1d = new ArrayList<>();
    private List<Data> candles_1w = new ArrayList<>();
    private List<Data> candles_1M = new ArrayList<>();

    {
        collectionsMap.put("btcusdt_1h", candles_1h);
        collectionsMap.put("btcusdt_4h", candles_4h);
        collectionsMap.put("btcusdt_1d", candles_1d);
        collectionsMap.put("btcusdt_1w", candles_1w);
        collectionsMap.put("btcusdt_1M", candles_1M);
    }*/
/*
    public void retrieveDataFromDb() {
        collectionsMap.forEach((collectionName, list) -> {
            List<Data> dataFromDb = (List<Data>) soaMongoHandler.findAll(Data.class, collectionName);
            list.addAll(dataFromDb);
        });
    }*/

    /**
    calculateSma() - вираховує SMA ковзна середня. Взято з інтернету. Проаналізувати!!!
     @param data - a list of the prices of each candle taken at the Close
     @param period - the number of candles on which the average is calculated.
     */
    public List<Double> calculateSma(List<Double> data, int period) {
        if (data.size() == 0) {
            throw new IllegalArgumentException("Empty data!");
        }
        if (period <= 0) {
            throw new IllegalArgumentException("Invalid period!");
        }

        double interm = 0;
        List<Double> result = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            interm += data.get(i);
            if (i + 1 < period) {
                result.add(Double.NaN);
            } else {
                result.add(interm / period);
                interm -= data.get(i + 1 - period);
            }
        }
        return result;
    }
}
