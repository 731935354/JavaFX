package whiteBoard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// --module-path /Users/rongxinzhu/Downloads/javafx-sdk-11.0.2/lib --add-modules javafx.controls,javafx.fxml

public class CreateWhiteBoard extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WhiteBoard.fxml"));
        Parent root = fxmlLoader.load();

        primaryStage.setTitle("Distributed Shared White Board");
        Scene scene = new Scene(root);
        scene.getStylesheets().add("whiteboard.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
