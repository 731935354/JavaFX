package JavaFXClientServer.Server;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ServerController {
    private static ServerController instance;

    @FXML
    private TextArea content;

    public ServerController() {
        instance = this;
    }

    public static ServerController getInstance() {
        return instance;
    }

    @FXML
    public void showText(String text) {
        /**
         * show text in the "content" text area.
         */
        Platform.runLater(() -> {  // 让controller和gui线程进行交互
            content.setText(text);
        });
    }
}
