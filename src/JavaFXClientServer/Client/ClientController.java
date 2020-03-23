package JavaFXClientServer.Client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

public class ClientController {
    @FXML
    private TextField textFieldWord;
    @FXML
    private TextField textFieldMeaning;
    @FXML
    private Button buttonAdd;

    @FXML
    private void addWord() {
        String word = textFieldWord.getText();
        String meaning = textFieldMeaning.getText();
        String request = "add-" + word + "-" + meaning;
        String response = ClientListener.sendMsg(request);
        System.out.println(response);
    }

//    @FXML
//    private void sendMsg() {
//        String content = textField.getText();
//        String response = ClientListener.sendMsg(content);
//        textFieldResponse.setText(response);
//    }

    public void showDialog(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setHeaderText(null);
            alert.showAndWait();
        });
    }
}
