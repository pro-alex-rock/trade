package com.home.trade.indicators.indicatorEMA;

import com.home.trade.indicators.Indicator;
import com.home.trade.indicators.indicatorMA.SimpleMovingAverage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 calculateEma() - вираховує EMA експоненціальна ковзна середня. Посилається на
 SMA {@link com.home.trade.indicators.indicatorMA.SimpleMovingAverage}
 Взято з інтернету. Проаналізувати!!!
 @param data - a list of the prices of each candle taken at the Close
 @param period - the number of candles on which the average is calculated.
 */

@Component
public class ExponentialMovingAverage implements Indicator {

    private final SimpleMovingAverage  simpleMovingAverage = new SimpleMovingAverage();

    public List<Double> calculateEma(List<Double> data, int period) {
        if (period <= 1) {
            throw new IllegalArgumentException("Invalid period");
        }

        List<Double> sma = simpleMovingAverage.calculateSma(data, period);
        double multiplier = 2.0 / (period + 1);
        List<Double> result = new ArrayList<>();

        for (int k = 0; k < sma.size(); k++) {
            Double v = sma.get(k);
            if (v.isNaN()) {
                result.add(Double.NaN);
            } else {
                double prev = result.get(k - 1);
                if (Double.isNaN(prev)) {
                    result.add(v);
                } else {
                    double ema = (data.get(k) - prev) * multiplier + prev;
                    result.add(ema);
                }
            }
        }
        return result;
    }
}
