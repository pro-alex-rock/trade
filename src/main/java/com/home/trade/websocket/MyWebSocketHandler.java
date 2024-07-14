package com.home.trade.websocket;

import com.home.trade.entity.enums.SocketEventTypeEnum;
import com.home.trade.entity.enums.TimeInterval;
import com.home.trade.service.NewDataProducer;
import com.home.trade.service.ProcessNewDataService;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@EqualsAndHashCode(callSuper=false)
@Log4j2
public class MyWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private NewDataProducer newDataProducer;

    private final String btcusdtSymbol = "btcusdt";
    private final String bnbbtcSymbol = "bnbbtc";

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // обробка вхідних повідомлень
        String messagePayload = message.getPayload();
        System.out.println("New Text Message Received: " + messagePayload);
        sendPayload(messagePayload);
    }

    private void sendPayload(String messagePayload) {
        newDataProducer.newDataReceived(messagePayload);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        newDataProducer.addObserver(new ProcessNewDataService());
        //newDataProducer.addObserver(new SimpleMovingAverage());
        // надсилання повідомлення після встановлення з'єднання
        //sendPing(session); //працює!
        //checkServerTime(session); //працює!
        //exchangeInformation(session); //працює! Цей запит слати через REST!!!
        //orderBook(session); //працює! Цей запит слати через REST!!!

        //subscribe(session, btcusdtSymbol + SocketEventTypeEnum.DEPTH.getLabel()); //subscribe on runtime stream to get tiks. btcusdt, bnbbtc
        //subscribe(session, btcusdtSymbol + SocketEventTypeEnum.KLINE.getLabel() + TimeInterval._1m.name()); //subscribe on runtime stream to get candles by interval. <--- WORKS!
        subscribe(session, btcusdtSymbol + SocketEventTypeEnum.KLINE.getLabel() + TimeInterval._1h.name()); //subscribe on runtime stream to get candles by interval. <--- WORKS!
        //listingSubscriptions(session);
        //subscribe(session, "bnbbtc@depth"); //subscribe on runtime stream to get tiks <--- WORKS!
        //subscribe(session, "btcusdt@aggTrade"); //subscribe on runtime stream to get tiks <--- WORKS!
    }

    private void subscribe(WebSocketSession session, String symbol) {
        JSONObject json = new JSONObject();
        json.put("method", "SUBSCRIBE");
        json.put("params", Collections.singletonList(symbol));
        json.put("id", Integer.valueOf(generateId()));

        TextMessage message = new TextMessage(json.toString());
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            throw new RuntimeException("Не вдалось надіслати повідомлення! " + e);
        }
    }

    private void listingSubscriptions(WebSocketSession session) {
        JSONObject request = new JSONObject();
        request.put("id", generateUUID());
        request.put("method", "LIST_SUBSCRIPTIONS");
        //JSONObject params = new JSONObject();
        //request.put("params", params);

        TextMessage message = new TextMessage(request.toString());
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            throw new RuntimeException("Не вдалось надіслати повідомлення для методу 'LIST_SUBSCRIPTIONS'! " + e);
        }
    }

    private void exchangeInformation(WebSocketSession session) {
        JSONObject request = new JSONObject();
        request.put("id", generateUUID());
        request.put("method", "exchangeInfo");
        JSONObject params = new JSONObject();
        params.put("symbols",new String[]{"BTCUSDT"});
        request.put("params", params);

        TextMessage message = new TextMessage(request.toString());
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            throw new RuntimeException("Не вдалось надіслати повідомлення для методу 'exchangeInfo'! " + e);
        }
    }

    private void orderBook(WebSocketSession session) {
        JSONObject request = new JSONObject();
        request.put("id", generateUUID());
        request.put("method", "depth");
        JSONObject params = new JSONObject();
        params.put("symbol","BTCUSDT");
        params.put("limit",5);
        request.put("params", params);

        TextMessage message = new TextMessage(request.toString());
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            throw new RuntimeException("Не вдалось надіслати повідомлення! " + e);
        }
    }

    public void sendPing(WebSocketSession session) {
        JSONObject request = new JSONObject();
        request.put("id", generateUUID());
        request.put("method", "ping");

        TextMessage message = new TextMessage(request.toString());
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            throw new RuntimeException("Не вдалось надіслати повідомлення! " + e);
        }
    }

    private void checkServerTime(WebSocketSession session) {
        JSONObject request = new JSONObject();
        request.put("id", generateUUID());
        request.put("method", "time");

        TextMessage message = new TextMessage(request.toString());
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            throw new RuntimeException("Не вдалось надіслати повідомлення! " + e);
        }
    }

    private String generateUUID() {
        return  UUID.randomUUID().toString();
    }

    private int generateId() {
        AtomicInteger currentId = new AtomicInteger(1);
        return currentId.getAndIncrement();
    }

}
