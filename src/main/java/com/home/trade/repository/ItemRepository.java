package com.home.trade.repository;

import com.home.trade.repository.entities.HistoryCandle;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepository extends MongoRepository<HistoryCandle, String> {

    public long count();
}
