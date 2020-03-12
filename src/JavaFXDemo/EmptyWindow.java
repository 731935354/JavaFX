package JavaFXDemo;

import javafx.application.Application;
import javafx.stage.Stage;


public class EmptyWindow extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("This is an empty window");
        primaryStage.show();
    }
}
