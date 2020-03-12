package JavaFXfxmlDemo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ClickButtonController {
    @FXML private TextField textField1;
    @FXML private TextField textField2;
    @FXML private Button button1;

    private int count = 0;


    @FXML
    private void clickButton() {
        count ++;
        if (count == 1) {
            textField1.setText("You just click the button");
        } else if (count == 2) {
            textField2.setText("You click again!");
        } else {
            textField2.setText("Please stop clicking.");
            textField1.setText("Please Stop clicking.");
        }
    }
}
