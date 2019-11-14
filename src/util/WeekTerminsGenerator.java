package util;

import Client.model.HairDresserTermin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class WeekTerminsGenerator {
    LocalDateTime start = null;


    public WeekTerminsGenerator(){
        this.start = LocalDateTime.now();
    }

    public WeekTerminsGenerator(LocalDateTime offset){
        this.start = offset;
    }

    public ObservableList<HairDresserTermin> generateTermins(){
        ObservableList<HairDresserTermin> TerminsBuffer = FXCollections.observableArrayList();
        LocalDateTime Time = this.start;
        Time = Time.withHour(10);
        Time = Time.withSecond(0);
        Time = Time.withMinute(0);
        while(Time.getDayOfWeek() == DayOfWeek.SUNDAY || Time.getDayOfWeek() == DayOfWeek.SATURDAY) {
            Time = Time.plusDays(1);
        }
        for (int day = 0; day < 5; day++) {

            for (int i = 10; i < 18; i++) {
                Time = Time.withHour(i);
                TerminsBuffer.add(new HairDresserTermin(Time));
            }
            if (Time.getDayOfWeek() == DayOfWeek.FRIDAY) {
                Time = Time.plusDays(3);
            } else {
                Time = Time.plusDays(1);
            }

        }
        return TerminsBuffer;
    }
}
