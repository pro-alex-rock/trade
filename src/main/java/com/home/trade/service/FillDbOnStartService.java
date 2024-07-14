package com.home.trade.service;

import com.home.trade.repository.entities.HistoryCandle;
import com.home.trade.service.MarketService.NoAuthService;
import com.home.trade.service.soa.SoaMongoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class FillDbOnStartService {

    //private ItemRepository itemRepository;

    private final SoaMongoManager soaMongoManager;
    private final NoAuthService noAuthService;
    private static final String[] intervals = {"1m", "3m", "5m", "15m", "30m", "1h", "2h", "4h", "6h", "8h", "12h", "1d", "3d", "1w", "1M"};
    private static final String[] baseIntervals = {"1h","4h","1d","1w", "1M"};
    private String symbol = "BTCUSDT";

    @Autowired
    public FillDbOnStartService(SoaMongoManager soaMongoManager, NoAuthService noAuthService) {
        this.soaMongoManager = soaMongoManager;
        this.noAuthService = noAuthService;
    }

    /*
        обов"язкова перевірка на наявність в колекції записів. якщо є, то починати по часу з останнього запису
        параметр 'часу з останнього запису' передавати в метод noAuthService.candlestick(symbol, interval)
         */
    public void fillHistoryDB() {
        Arrays.stream(baseIntervals)
                .forEach(interval -> {
                    Optional<HistoryCandle> isRecordExists = checkIsRecordExists(symbol.toLowerCase() + "_" + interval);
                    List<HistoryCandle> list = noAuthService.candlestick(symbol, interval, isRecordExists);
                    if(isRecordExists.isEmpty()) {
                        saveToDB(list, symbol.toLowerCase() + "_" + interval);
                    } else {
                        saveToDB(list.subList(1, list.size()), symbol.toLowerCase() + "_" + interval);
                    }
                });
    }

    private Optional<HistoryCandle> checkIsRecordExists(String collectionName) {
        if (!soaMongoManager.isCollectionExists(collectionName)) {
            return Optional.empty();
        }
        Query query = new Query().with(Sort.by(Sort.Order.desc("timestamp"))).limit(1);
        List<HistoryCandle> candles = (List<HistoryCandle>) soaMongoManager.find(query, HistoryCandle.class, collectionName);

        if (!candles.isEmpty()) {
            return Optional.of(candles.get(0));
        } else {
            return Optional.empty();
        }
    }

    private void saveToDB(List<HistoryCandle> candles, String collectionName) {
        if (!soaMongoManager.isCollectionExists(collectionName)) {
            soaMongoManager.createCollection(collectionName);
        }
        soaMongoManager.insert(candles, collectionName);
    }
}
