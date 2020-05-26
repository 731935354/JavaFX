package whiteBoard;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.StrokeLineJoin;

public class WhiteBoardController {
    // drawing area
    @FXML Pane drawingPane;

    // mode
    @FXML Button drawModeButton;
    @FXML Button editModeButton;

    // shape
    @FXML Spinner<Double> lineWidthSpinner;
    @FXML ColorPicker lineColorPicker;
    @FXML ColorPicker fillColorPicker;

    @FXML ComboBox<String> toolTypeComboBox;
    @FXML ComboBox<String> fontFamilyComboBox;
    @FXML Spinner<Integer> fontSizeSpinner;

    // text
    @FXML ColorPicker fontColorPicker;
    @FXML CheckBox fontBoldCheckBox;
    @FXML CheckBox fontItalicCheckBox;

    private ToolType toolType;
    private double x1, y1, x2, y2;
    private Path freeDraw;
    private double strokeWidth;
    private Color strokeColor;


    @FXML
    private void initialize() {
        // default settings
        toolType = ToolType.PEN;
        strokeColor = Color.BLACK;
        strokeWidth = 1;
        // draw mode by default
        drawMode();
    }

    public void drawMode() {
        drawingPane.setOnMousePressed(e -> {
            x1 = e.getX();
            y1 = e.getY();
            if (toolType == ToolType.PEN) {
                freeDraw = new Path();  // 新建自由绘制对象
                freeDraw.getElements().add(new MoveTo(x1, y1));  // 设定自由绘制起点
                freeDraw.setStrokeLineJoin(StrokeLineJoin.ROUND);
                drawingPane.getChildren().add(freeDraw);  // 将自由绘制对象添加至画板中
            }
        });
        drawingPane.setOnMouseDragged(e -> {
            x2 = e.getX();
            y2 = e.getY();
            if (toolType == ToolType.PEN) {
                freeDraw.getElements().add(new LineTo(x2, y2));  // 向自由绘制对象中添加元素(线段)
                freeDraw.setStroke(strokeColor);  // 设置画笔颜色
                freeDraw.setStrokeWidth(strokeWidth); // 设置画笔粗细
                freeDraw.getStyleClass().add("shape");
            }
        });
    }

    public void editMode() {
        return;
    }
}
