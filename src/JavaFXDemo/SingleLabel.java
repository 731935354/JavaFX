package JavaFXDemo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

// --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml
// PATH_TO_FX=/Users/rongxinzhu/Downloads/javafx-sdk-11.0.2/lib

public class SingleLabel extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Hello there!");
        Scene scene = new Scene(label, 200, 400);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
