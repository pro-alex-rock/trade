package com.home.trade.configuration;

import com.home.trade.websocket.MyWebSocketHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.util.concurrent.ExecutionException;

/*@Configuration
public class WebSocketConfig implements ApplicationListener<ContextRefreshedEvent> {

    private final WebSocketHandler webSocketHandler;

    @Value("${spring.binance.test.ws.url}")
    private String baseUrl;

    @Value("${spring.binance.test.ws.stream.url}")
    private String streamUrl;

    public WebSocketConfig(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Bean
    public WebSocketClient webSocketClient() {
        return new StandardWebSocketClient();
    }

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new  MyWebSocketHandler();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        WebSocketClient webSocketClient = event.getApplicationContext().getBean(WebSocketClient.class);
        try {
            //webSocketClient.doHandshake(webSocketHandler, baseUrl).get(); //працює!!! для одиничних запитів типу orderBook
            webSocketClient.doHandshake(webSocketHandler, streamUrl).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}*/

@Configuration
public class WebSocketConfig {

    private final WebSocketHandler webSocketHandler;

    @Value("${spring.binance.test.ws.url}")
    private String baseUrl;

    @Value("${spring.binance.test.ws.stream.url}")
    private String streamUrl;

    public WebSocketConfig(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Bean
    public WebSocketClient webSocketClient() {
        return new StandardWebSocketClient();
    }

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new MyWebSocketHandler();
    }

    public void initializeWebSocketConnection() {
        WebSocketClient webSocketClient = webSocketClient();
        try {
            webSocketClient.doHandshake(webSocketHandler, streamUrl).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}