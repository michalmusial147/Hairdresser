package Client;

import Client.model.HairDresserTermin;
import Client.util.DateUtil;
import Client.view.Controller;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.collections.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

public class StartClient extends Application {


    private String ClientName = "Michu";
    private AnchorPane ClientLayout;
    private Stage primaryStage;
    private ObservableList<HairDresserTermin> Termins = generateTermins();
    private ArrayList<HairDresserTermin> Reservations = new ArrayList<>();
    private HairDresserTermin ReservatedTermin = null;
    private Thread ClientNetThread ;
    private Controller controller;

    public void setReservations(ArrayList<HairDresserTermin> reservations) {
        Reservations.addAll(reservations);
        if(controller!=null){
            controller.load_columns();
        }
    }

    public StartClient()  {
    }

    public ObservableList<HairDresserTermin> generateTermins(){
        ObservableList<HairDresserTermin> TerminsBuffer = FXCollections.observableArrayList();
        LocalDateTime Time = LocalDateTime.now();
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
    public void setTermins(ObservableList<HairDresserTermin> Termins) {
       this.Termins = Termins;
        if(controller!=null) {
          controller.setTables();
        }
    }
    public void setTermins(List<HairDresserTermin> Termins) {
        ObservableList<HairDresserTermin> buf = FXCollections.observableArrayList();
        buf.addAll(Termins);
        setTermins(buf);
    }

    @Override
    public void start(Stage primaryStage) throws IOException,InterruptedException {
        ClientNetThread = new Thread(new ClientNetThread(ClientName,this));
        ClientNetThread.start();
        ClientNetThread.join();
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("HairDresserClient");
        initLayout();
        setTermins(generateTermins());
    }

    public void addReservation(LocalDateTime ReservationTime) {
        if (ReservatedTermin != null) {
            return;
        }
        if (isReservated(ReservationTime)) {
            System.out.println("Reservated before: " + DateUtil.format(ReservationTime));
        } else {
            this.ReservatedTermin = new HairDresserTermin(ReservationTime);

            this.Reservations.add(new HairDresserTermin(ReservationTime));
            System.out.println("Reservation: " + DateUtil.format(ReservationTime));
        }
    }

    public void cancelReservation() {
        if (ReservatedTermin == null || Reservations==null) {
           return;
        }
        if(Reservations.size()==0){
            return;
        }
        for (HairDresserTermin x : Reservations) {
            if (DateUtil.format(x.TerminTime()).equals( DateUtil.format(ReservatedTermin.TerminTime()))) {
                Reservations.remove(x);
            }
        }
        ReservatedTermin = null;
    }

    public boolean isReservated(LocalDateTime t) {
        for (HairDresserTermin x : this.Reservations) {
            if (DateUtil.format(x.TerminTime()).equals(DateUtil.format(t))) {
                return true;
            }
        }
        return false;
    }

    public void initLayout() {
        try {
            // Client root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(StartClient.class
                    .getResource("view/ClientGUI.fxml"));
            ClientLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(ClientLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            controller = loader.getController();
            controller.setMainApp(this);
            controller.setTables();
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<HairDresserTermin> getTermins() {
        return Termins;
    }

    public ObservableList<HairDresserTermin> getDay(int DayOfWeek) {
        ObservableList<HairDresserTermin> TerminsOfDay = FXCollections.observableArrayList();
        for (int offset = DayOfWeek * 8, iter = 0; iter < 8; iter++) {
              TerminsOfDay.add(Termins.get(offset+iter));
        }
        return TerminsOfDay;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public String getClientName() {
        return ClientName;
    }
}