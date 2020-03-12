package JavaFXDemo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;


import java.util.List;

/* run this in terminal:
*  compile: in /path/to/project/src/JavaFXDemo
*  javac --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml CommandLineParas.java
*  run: in /path/to/project/src
*  java --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml CommandLineParas
* */

public class CommandLineParas extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // get command line arguments
        Parameters parameters = getParameters();
        List<String> paras = parameters.getUnnamed();

        primaryStage.setTitle("Demo of receiving command line parameters.");

        Label label = new Label();

        if (paras.size() > 0) {
            label.setText("Receive first para: " + paras.get(0));
        } else {
            label.setText("No paras received.");
        }

        Scene scene = new Scene(label, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
