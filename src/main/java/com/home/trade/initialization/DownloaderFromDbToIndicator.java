package com.home.trade.initialization;

import com.home.trade.service.ProcessNewDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Take all history-data from DB to indicator
 * This action made for consuming data by Indicator in order:
 * 1 - all from DB. {@link com.home.trade.initialization.StartupDataToDbLoader}
 * 2 - load all data from DB to indicator {@link com.home.trade.initialization.DownloaderFromDbToIndicator}
 * 3 - all ticks from websocket in runtime mode {@link com.home.trade.initialization.WebSocketInitializer}
 */
@Component
@Order(2)
public class DownloaderFromDbToIndicator implements CommandLineRunner {

    @Autowired
    ProcessNewDataService dataService;
    @Override
    public void run(String... args) throws Exception {
        dataService.retrieveDataFromDb();
    }
}
