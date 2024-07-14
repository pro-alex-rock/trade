package com.home.trade.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.trade.entity.NewDataObserver;
import com.home.trade.entity.KlinePayload;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * On prod must send all input data to Kafka;
 * On test sends input data to indicators through temporary replacement of kafka - class **
 */


@Component
public class NewDataProducer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<NewDataObserver> observers = new ArrayList<>();

    public void addObserver(NewDataObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(NewDataObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(KlinePayload data) {
        for (NewDataObserver observer : observers) {
            observer.update(data);
        }
    }

    public int getSizeObservers() {
        return observers.size();
    }

    public void newDataReceived(String data) {
        System.out.println("NewDataProducer new data received! " + data);
        KlinePayload klinePayload;
        try {
            klinePayload = objectMapper.readValue(data, KlinePayload.class);
            System.out.println("Symbol = " + klinePayload.getSymbol()); // Для перевірки
        } catch (Exception e) {
            throw new RuntimeException("Can't parse payload: " + data + "\n" + e);
        }
        notifyObservers(klinePayload);
    }
}
