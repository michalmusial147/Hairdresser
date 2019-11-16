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
import util.WeekTerminsGenerator;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


public class StartServer extends Application {
    private Map<InetSocketAddress, String> users = new HashMap<>();
    private AnchorPane ClientLayout;
    private Stage primaryStage;
    private ObservableList<HairDresserTermin> Termins;
    private  Controller controller;

    private ArrayList<HairDresserTermin> Reservations = new ArrayList<>();
    //private HairDresserTermin ReservatedTermin = null;
    private Thread Server;
    private ObservableList<String> Users = FXCollections.observableArrayList();

    public ArrayList<HairDresserTermin> getReservations() {
        return Reservations;
    }

    @Override
    public void start(Stage primaryStage) {
        Termins = new WeekTerminsGenerator().generateTermins();
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("HairDresserServer");
        initLayout();
        try{
            Server = new Thread(new ServerNet(this.Reservations, this.controller));
            Server.start();
        }
        catch(Exception e){
            e.printStackTrace();
        }
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
            this.controller = loader.getController();
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
