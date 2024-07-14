package com.home.trade.initialization;

import com.home.trade.service.FillDbOnStartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Fills DB with the history of all ticks
 * immediately after starting the program at all intervals;
 * This action made for consuming data by Indicator in order:
 * 1 - all from DB. {@link com.home.trade.initialization.StartupDataToDbLoader}
 * 2 - load all data from DB to indicator {@link com.home.trade.initialization.DownloaderFromDbToIndicator}
 * 3 - all ticks from websocket in runtime mode {@link com.home.trade.initialization.WebSocketInitializer}
 */

@Component
@Order(1)
public class StartupDataToDbLoader implements CommandLineRunner {

    @Autowired
    private FillDbOnStartService startService;

    @Override
    public void run(String... args) throws Exception {
        startService.fillHistoryDB();
    }
}
