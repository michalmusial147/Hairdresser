package Client;

import Client.model.HairDresserTermin;
import util.DateUtil;
import Client.view.Controller;
import util.Operation;
import util.WeekTerminsGenerator;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.collections.*;


import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

public class StartClient extends Application {
    private int clientListeningPort = 15452;
    private String ClientName = "Michu";
    private AnchorPane ClientLayout;
    private Stage primaryStage;
    private ObservableList<HairDresserTermin> Termins;
    private ArrayList<HairDresserTermin> Reservations = new ArrayList<>();
    private HairDresserTermin ReservatedTermin = null;

    private Controller controller;

    public void setReservations(ArrayList<HairDresserTermin> reservations) {
        Reservations.addAll(reservations);
        if(controller!=null){
            controller.init_columns();
        }
    }

    public StartClient()  {
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
        Thread GetTerminsThread;
        ClientNetThread getTermins = new ClientNetThread(controller, ClientName,Reservations, Operation.GETTERMINS, this.clientListeningPort);
        GetTerminsThread = new Thread(getTermins);
        GetTerminsThread.start();
        GetTerminsThread.join();
        this.Reservations = getTermins.getReservations();
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("HairDresserClient");
        initLayout();
        new Thread(new ListeningThread(controller, ClientName,Reservations, Operation.GETTERMINS,
                this.clientListeningPort)).start();

        setTermins(new WeekTerminsGenerator().generateTermins());
    }

    public void addReservation(LocalDateTime ReservationTime) throws IOException, InterruptedException {
        if (ReservatedTermin != null) {
            return;}
        if (isReservated(ReservationTime)) {
            controller.showAlert("Termin zajety", "Wybrano zajety termin",
                    "Prosze wybrac inny termin wizyty");
            System.out.println("Reservated before: " + DateUtil.format(ReservationTime));
            return;
        }
        boolean result = false;
        ClientNetThread c =  new ClientNetThread(controller, ClientName,
                new HairDresserTermin(ReservationTime), Operation.REGISTER,this.clientListeningPort);
        Thread t = new Thread(c);
        t.start();
        t.join();
        result = c.isRegisterOK();
        if (result == false) {
            controller.showAlert("Termin zajety", "Wybrano zajety termin",
                    "Prosze wybrac inny termin wizyty");
            return;
        }
        else {
            this.ReservatedTermin = new HairDresserTermin(ReservationTime);
            this.Reservations.add(new HairDresserTermin(ReservationTime));
            System.out.println("Reservation: " + DateUtil.format(ReservationTime));
            return;
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

    public Stage getPrimaryStage() {
        return this.primaryStage;
    }
}