package JavaFXClientServer.Client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ClientController {
    @FXML
    private TextField textField;
    @FXML
    private TextField textFieldResponse;
    @FXML
    private Button sendButton;

    @FXML
    private void sendMsg() {
        String content = textField.getText();
        String response = ClientListener.sendMsg(content);
        textFieldResponse.setText(response);
    }
}
