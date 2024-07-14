package com.home.trade.orderAnalyser.patterns;

import com.home.trade.entity.AnalyzerObserver;
import com.home.trade.entity.KlinePayload;
import com.home.trade.service.ProcessNewDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MorningStarPattern implements AnalyzerObserver {
    @Autowired
    private ProcessNewDataService processNewDataService;

    private boolean isEveningStar;
    private boolean morePower; //TODO якщо є розриви з двох сторін, якщо це бича свічка

    {
        processNewDataService.addObserver(this);
    }

    public boolean isEveningStar() { //TODO ендпоінт до Акумулятора. Можливо Observer чи Spring Events (1)
        return isEveningStar;
    }

    public boolean isMorePower() { //TODO ендпоінт до Акумулятора. Можливо Observer чи Spring Events (2)
        return morePower;
    }

    @Override
    public void reCalculate(KlinePayload klinePayload) {

    }
}
