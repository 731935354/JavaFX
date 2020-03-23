package JavaFXClientServer.Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class ClientLauncher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ClientLayout.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        // get command line arguments
        Parameters parameters = getParameters();
        List<String> paras = parameters.getUnnamed();

        String ip = paras.get(0);
        int port = Integer.parseInt(paras.get(1));
        System.out.println("ip: " + ip);
        System.out.println("port: " + port);

        // client listener is responsible for dealing with files and network input/outputs

        ClientListener clientListener = new ClientListener("127.0.0.1", 9992);
        Thread x = new Thread(clientListener);
        x.start();
    }
}
