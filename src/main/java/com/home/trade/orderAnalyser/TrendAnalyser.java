package com.home.trade.orderAnalyser;

import com.home.trade.entity.enums.TrendEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class TrendAnalyser {

    @Autowired
    private VolumeAnalyzer volumeAnalyzer;
    private EmaAnalyser.EmaData emaData;
    private List<Double> volumes;

    /**
     * For example, if you have a list of EMA and volume data for the last 100 days,
     * the index 99 will point to the last day (provided your lists start with 0).
     * TrendEnum todayTrend = analyser.getTrend(99); - get the trend for the last day
     * @param index - int. The last interval for now (hour, day etc)
     * @return
     */
    public TrendEnum getTrend(String interval) {
        if (null == interval || interval.equals("")) {
            throw new RuntimeException("The interval cannot be empty!");
        }
        volumes = volumeAnalyzer.getVolumeByInterval(interval);

        List<Double> shortEma = emaData.getShortEma();
        List<Double> mediumEma = emaData.getMediumEma();
        List<Double> longEma = emaData.getLongEma();
        int shortEmaSize = shortEma.size();
        int mediumEmaSize = mediumEma.size();
        int longEmaSize = longEma.size();

        boolean isEma7AboveEma25 = shortEma.get(shortEmaSize - 1) > mediumEma.get(mediumEmaSize - 1);
        boolean isEma7AboveEma50 = shortEma.get(shortEmaSize - 1) > longEma.get(longEmaSize - 1);
        boolean isVolumeIncreasing = isRecentVolumeSignificantlyHigher();

        if (isEma7AboveEma25 && isEma7AboveEma50 && isVolumeIncreasing) {
            return TrendEnum.BULLISH;
        } else if (!isEma7AboveEma25 && !isEma7AboveEma50 && isVolumeIncreasing) {
            return TrendEnum.BEARISH;
        } else {
            return TrendEnum.FLAT;
        }
    }

    /**
     * This method returns true if the volume of the last interval is more than 10% higher
     * than the average volume of the previous 20 intervals.
     * @return
     */
    public boolean isRecentVolumeSignificantlyHigher() {
        if (volumes.size() < 21) { //TODO 21 - для тесту. визначити вірну кількість!
            throw new RuntimeException("Not enough data for analysis");
        }

        // Визначення середнього об'єму за попередні 20 інтервалів
        double averageOfPrevious20 = volumes.subList(volumes.size() - 21, volumes.size() - 1)
                .stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        // Отримання об'єму останнього інтервалу
        double lastVolume = volumes.get(volumes.size() - 1);

        // Перевірка, чи об'єм останнього інтервалу на 10% вищий за середній рівень попередніх 20 інтервалів
        return lastVolume > 1.10 * averageOfPrevious20;
    }

}
