package Client.model;

import java.time.LocalDateTime;
import javafx.beans.property.StringProperty;

public class HairDresserTermin {
    private final StringProperty ClientName;
    private final StringProperty TerminTime;

    public HairDresserTermin() {
        this.ClientName = null;
        this.TerminTime = null;
    }
    public HairDresserTermin(StringProperty ClientNameIn,StringProperty TerminTimeIn)
    {
        this.ClientName = ClientNameIn;
        this.TerminTime = TerminTimeIn;
    }




}
