package Server;

import Client.model.HairDresserTermin;
import util.DateUtil;
import Server.view.Controller;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.collections.*;
import java.util.ArrayList;
import java.time.DayOfWeek;
import java.time.LocalDateTime;


public class StartServer extends Application {

    private AnchorPane ClientLayout;
    private Stage primaryStage;
    private ObservableList<HairDresserTermin> Termins = generateTermins();


    private ArrayList<HairDresserTermin> Reservations = new ArrayList<>();
    //private HairDresserTermin ReservatedTermin = null;
    private Thread Server;
    private ObservableList<String> Users = FXCollections.observableArrayList();

    public ArrayList<HairDresserTermin> getReservations() {
        return Reservations;
    }
    public ObservableList<HairDresserTermin> generateTermins(){
        ObservableList<HairDresserTermin> TerminsBuffer = FXCollections.observableArrayList();
        LocalDateTime Time = LocalDateTime.now();
        Time = Time.withHour(10);
        Time = Time.withSecond(0);
        Time = Time.withMinute(0);
        while(Time.getDayOfWeek() == DayOfWeek.SUNDAY || Time.getDayOfWeek() == DayOfWeek.SATURDAY){
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

    @Override
    public void start(Stage primaryStage) {
        try{
            Server = new Thread(new ServerNet(this.Reservations));
            Server.start();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("HairDresserServer");
        initLayout();
    }

    public void addReservation(LocalDateTime ReservationTime){

    }
    public void cancelReservation()
    {

    }
    public boolean isReservated(LocalDateTime t){
        for(HairDresserTermin x : this.Reservations) {
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
            loader.setLocation(StartServer.class
                    .getResource("view/ServerGUI.fxml"));
            ClientLayout =  loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(ClientLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            Controller controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addUser (String Name){
        Users.add(Name);
    }


    public ObservableList<HairDresserTermin> getTermins() {
        return Termins;
    }
    public ObservableList<HairDresserTermin> getDay(int DayOfWeek) {
        ObservableList<HairDresserTermin> TerminsOfDay = FXCollections.observableArrayList();
        for(int offset = DayOfWeek*8, iter = 0; iter<8; iter++){
            TerminsOfDay.add(Termins.get(offset+iter));
        }
        return TerminsOfDay;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
