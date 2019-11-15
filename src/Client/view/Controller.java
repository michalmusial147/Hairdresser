/**
 * TO DO alerty ze juz jest zarezerwowane i potwierdzenie zarezerwowania
 */

package Client.view;
import Client.StartClient;
import Client.model.HairDresserTermin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDateTime;

import util.DateUtil;

public class Controller {
    // Reference to the main application
    public StartClient StartClientMain;

    private ObservableList<HairDresserTermin> Termins;

    public void setReservations(ObservableList<HairDresserTermin> reservations) {
        Reservations = reservations;
    }

    private ObservableList<HairDresserTermin> Reservations;
    @FXML
    private TableView<HairDresserTermin> TerminTable1;
    @FXML
    private TableView<HairDresserTermin> TerminTable2;
    @FXML
    private TableView<HairDresserTermin> TerminTable3;
    @FXML
    private TableView<HairDresserTermin> TerminTable4;
    @FXML
    private TableView<HairDresserTermin> TerminTable5;
    @FXML
    private TableColumn<HairDresserTermin, LocalDateTime> Column1;
    @FXML
    private TableColumn<HairDresserTermin, LocalDateTime> Column2;
    @FXML
    private TableColumn<HairDresserTermin, LocalDateTime> Column3;
    @FXML
    private TableColumn<HairDresserTermin, LocalDateTime> Column4;
    @FXML
    private TableColumn<HairDresserTermin, LocalDateTime> Column5;
    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public Controller(){}

    @FXML
    private void initialize() {
       // Termins.addListener();
        init_columns();
        add_columns_listeners();

    }
    public void init_columns(){
        init_column(Column1);
        init_column(Column2);
        init_column(Column3);
        init_column(Column4);
        init_column(Column5);
    }
    private void add_columns_listeners(){
        TerminTable1.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> SendReservationRequest(newValue));
        TerminTable1.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TerminTable2.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> SendReservationRequest(newValue));
        TerminTable2.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TerminTable3.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> SendReservationRequest(newValue));
        TerminTable3.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TerminTable4.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> SendReservationRequest(newValue));
        TerminTable4.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TerminTable5.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> SendReservationRequest(newValue));
        TerminTable5.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }
    public void init_column(TableColumn<HairDresserTermin, LocalDateTime> col) {
         col.setCellValueFactory(cellData ->
                cellData.getValue().TerminTimeProperty());
         col.setCellFactory(column -> {
                return new TableCell<HairDresserTermin, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    }
                    else {
                        // Format date.
                        setText(DateUtil.format(item));
                        if(StartClientMain.isReservated(item)) {
                            setStyle("-fx-background-color: red");
                        }
                    }
                }

                };
        });
    }

    /**
     * TO DO alert ze juz jest zarezerwowane
     */
   private void SendReservationRequest(HairDresserTermin termin){
       try {
           StartClientMain.addReservation(termin.TerminTime());
       }
       catch( IOException | InterruptedException e){e.printStackTrace();
       }
       init_columns();

   }
   @FXML
   private void handleCancelReservation() {
        StartClientMain.cancelReservation();
        init_columns();
   }
    public ObservableList<HairDresserTermin> getDay(int DayOfWeek, ObservableList<HairDresserTermin> TerminsToSplit) {
        if(TerminsToSplit == null) return null;
       if(TerminsToSplit.size()==0) return null;
        ObservableList<HairDresserTermin> TerminsOfDay = FXCollections.observableArrayList();
        for (int offset = DayOfWeek * 8, iter = 0; iter < 8; iter++) {
              TerminsOfDay.add(TerminsToSplit.get(offset+iter));
        }
        return TerminsOfDay;
    }
    public void setMainApp(StartClient StartClientMain) {
        this.StartClientMain = StartClientMain;
        setTables();
    }
    public void setTables(){
        Termins = StartClientMain.getTermins();
        TerminTable1.setItems(getDay(0, Termins));
        TerminTable2.setItems(getDay(1, Termins));
        TerminTable3.setItems(getDay(2, Termins));
        TerminTable4.setItems(getDay(3, Termins));
        TerminTable5.setItems(getDay(4, Termins));
        init_columns();
    }
    public void showAlert(String title, String headerText, String contentText){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(StartClientMain.getPrimaryStage());
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }


}
