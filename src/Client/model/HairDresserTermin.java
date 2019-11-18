package Client.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.StringProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;


public class HairDresserTermin implements Serializable {
    private transient final ObjectProperty<LocalDateTime> TerminTime;
    public HairDresserTermin() {
        this.TerminTime = null;
    }
    public HairDresserTermin(LocalDateTime TerminTimeIn) {
        this.TerminTime = new SimpleObjectProperty<LocalDateTime>(TerminTimeIn);
    }
    public  ObjectProperty<LocalDateTime> TerminTimeProperty(){
        return this.TerminTime;
    }
    public LocalDateTime TerminTime(){
        return this.TerminTime.get();
    }
    public String TerminTimeString(){
        return this.TerminTime.get().format(DateTimeFormatter.ISO_DATE_TIME);
    }
    public StringProperty TerminTimeStringProperty() {

        return new SimpleStringProperty(TerminTimeString());
    }
}
