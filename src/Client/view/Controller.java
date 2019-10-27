/**
 * TO DO alerty ze juz jest zarezerwowane i potwierdzenie zarezerwowania
 */

package Client.view;
import Client.StartClient;
import Client.model.HairDresserTermin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SelectionModel;
import Client.util.DateUtil;
import javafx.scene.control.TableCell;
import javafx.scene.input.MouseEvent;
public class Controller {
    // Reference to the main application
    public StartClient StartClientMain;
    private ObservableList<HairDresserTermin> Termins;
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
        load_columns();
        add_columns_listeners();
    }
    private void load_columns(){
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
    private void init_column(TableColumn<HairDresserTermin, LocalDateTime> col) {
        // Initialize the person table with one column.
         col.setCellValueFactory(cellData ->
                cellData.getValue().TerminTimeProperty());
        // Custom rendering of the table cell.
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
       StartClientMain.addReservation(termin.TerminTime());
       load_columns();

   }

   @FXML
   private void handleCancelReservation() {
        StartClientMain.cancelReservation();
        load_columns();
   }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param StartClientMain
     */
    public void setMainApp(StartClient StartClientMain) {
        this.StartClientMain = StartClientMain;
        TerminTable1.setItems(StartClientMain.getDay(0));
        TerminTable2.setItems(StartClientMain.getDay(1));
        TerminTable3.setItems(StartClientMain.getDay(2));
        TerminTable4.setItems(StartClientMain.getDay(3));
        TerminTable5.setItems(StartClientMain.getDay(4));
    }

}
