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
        // 获取单词和词义
        String word = textFieldWord.getText();
        String meaning = textFieldMeaning.getText();

        // 构建request 形如“add-apple-a kind of fruit”
        String request = "add-" + word + "-" + meaning;

        // 将request发给服务器(视图层/控制层ClientController 会调用数据层ClientListener的逻辑)
        String response = ClientListener.sendMsg(request);

        // 根据回复进行不同的操作
        // 单词太短，弹出相应提示
        if (response.equals("word too short")) {
            popHint("Word is too short, please try again.");
        } else if (response.equals("add success")) {
            popHint("Word <" + word + "> has been added successfully.");
        }

        System.out.println(response);
    }

//    @FXML
//    private void sendMsg() {
//        String content = textField.getText();
//        String response = ClientListener.sendMsg(content);
//        textFieldResponse.setText(response);
//    }

    /* 弹窗 */
    public void popHint(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setHeaderText(null);
            alert.showAndWait();
        });
    }
}
