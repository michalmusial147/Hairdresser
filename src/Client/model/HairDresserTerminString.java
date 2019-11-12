package Client.model;
import Client.model.HairDresserTermin;
import Client.util.DateUtil;
import java.io.Serializable;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;

import javafx.beans.property.StringProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;


public class HairDresserTerminString implements Serializable {
    private String TerminTime;
    public HairDresserTerminString() {
        this.TerminTime = null;
    }
    public HairDresserTerminString(LocalDateTime TerminTimeIn) {
        this.TerminTime = DateUtil.format(TerminTimeIn);
    }
    public HairDresserTerminString(HairDresserTermin termins){
        TerminTime = DateUtil.format(termins.TerminTime());
    }
    public String TerminTimeString(){
        return TerminTime;
    }
    public StringProperty TerminTimeStringProperty() {
        //  System.out.println(new SimpleStringProperty(TerminTimeString()));
        return new SimpleStringProperty(TerminTimeString());
    }

}
