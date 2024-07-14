package com.home.trade.initialization;

import com.home.trade.configuration.WebSocketConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Take all tick-data from Binance to indicator through Websocket
 * after subscribing.
 * This action made for consuming data by Indicator in order:
 * 1 - all from DB. {@link com.home.trade.initialization.StartupDataToDbLoader}
 * 2 - load all data from DB to indicator {@link com.home.trade.initialization.DownloaderFromDbToIndicator}
 * 3 - all ticks from websocket in runtime mode {@link com.home.trade.initialization.WebSocketInitializer}
 */
@Component
@Order(3)
public class WebSocketInitializer implements CommandLineRunner {

    @Autowired
    private WebSocketConfig webSocketConfig;

    @Override
    public void run(String... args) throws Exception {
        webSocketConfig.initializeWebSocketConnection();
    }
}
