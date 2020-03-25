package JavaFXClientServer.Server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import multi_turn.Server;

public class ServerLauncher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // load and show window
        Parent root = FXMLLoader.load(getClass().getResource("ServerLayout.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        int port = 9993;
        String dictPath = "/Users/rongxinzhu/IdeaProjects/socket_example/src/JavaFXClientServer/JsonIO/example.json";
        ServerListener serverListener = new ServerListener(port, dictPath);
        Thread thread = new Thread(serverListener);
        thread.start();
    }
}
