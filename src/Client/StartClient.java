package Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.collections.*;
import Client.view.Controller;

public class StartClient extends Application {

    private AnchorPane ClientLayout;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/ClientGUI.fxml"));
        primaryStage.setTitle("");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public void initRootLayout() {
        try {
            // Client root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(StartClient.class
                    .getResource("view/ClientGUI.fxml"));
            ClientLayout = (AnchorPane) loader.load();

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
    public static void main(String[] args) {
        launch(args);
    }
}
