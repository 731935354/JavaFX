/*
已实现功能：
1. 绘图模式：绘制矩形，自由曲线
2. 编辑模式：移动矩形，自由曲线
3. 特效：鼠标放在形状上会有高亮效果
4. 油漆桶工具，填充形状
5. 填充矩形为几乎透明，从而让整个矩形都可以捕捉鼠标点击和拖动事件(不再必须点击和拖拽边框)
6. 添加文字
7. 设置字体，字号
 */

import Shapes.MovableResizableRecAsGroup;
import Shapes.MovableResizableRectangle;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class Demo20200526 extends Application {
    private double x1, y1, x2, y2;
    private Path freeDraw;
//    private Rectangle rectangle;
    private MovableResizableRectangle rectangle;
    private double oriX, oriY, oriTranslateX, oriTranslateY;
    private Pane pane;
    private Button btn, btnBack;
    private Label strokWidthLabel;
    private TextField textInput;
    private Text textElement, selectedText;
    private ToolType toolType;
    private double strokeWidth = 1;
    private Color strokeColor = Color.BLACK;
    private Spinner<Integer> fontSizeSpinner;
    private ComboBox<String> fontFamilySelector;
    private ColorPicker cp;
    private String mode = "draw";


    @Override
    public void start(Stage primaryStage) {
        pane = new Pane();

        // 撤销按钮
        btnBack = new Button("Undo");
        btnBack.setOnAction(e -> {
            ObservableList<Node> children = pane.getChildren();
            int numChildren = children.size();
            Node lastAddedNode = children.get(numChildren - 1);
            if (lastAddedNode instanceof Shape)
                pane.getChildren().remove(numChildren - 1);
        });
        btnBack.setLayoutX(510);
        btnBack.setLayoutY(0);
        pane.getChildren().add(btnBack);


        // 模式切换按钮
        btn = new Button("switch to edit mode");
        pane.getChildren().add(btn);

        // 颜色选择器
        cp = new ColorPicker();
        Slider slider = new Slider();

        cp.setValue(Color.BLACK);
        cp.setOnAction(e -> {
            strokeColor = cp.getValue();
            System.out.println("Color: " + strokeColor.toString());
        });
        cp.valueProperty().addListener(event -> changeFont()); // 改变字体颜色
        cp.setLayoutX(0);
        cp.setLayoutY(50);
        pane.getChildren().add(cp);

        // 画笔大小
        slider.setMin(1);
        slider.setMax(100);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.valueProperty().addListener(e->{
            double value = slider.getValue();
            String str = String.format("%.1f", value);
            strokWidthLabel.setText(str);
            strokeWidth = value;
        });
        slider.setLayoutX(150);
        slider.setLayoutY(50);
        strokWidthLabel = new Label("1");
        strokWidthLabel.setLayoutX(300);
        strokWidthLabel.setLayoutY(50);
        pane.getChildren().addAll(slider, strokWidthLabel);

        // 默认设置
        drawMode();  // 绘图模式
        toolType = ToolType.PEN;  // 自由画笔工具

        // 不同画笔工具按钮
        // 矩形工具
        Button drawRec = new Button("Rectangle");
        drawRec.setOnAction(e -> {
            toolType = ToolType.RECTANGLE;
        });
        drawRec.setLayoutX(150);
        drawRec.setLayoutY(0);

        // 自由画笔工具
        Button usePen = new Button("Pen");
        usePen.setOnAction(e -> {
            toolType = ToolType.PEN;
        });
        usePen.setLayoutX(260);
        usePen.setLayoutY(0);

        pane.getChildren().addAll(usePen, drawRec);

        // 油漆桶工具
        Button useBucket = new Button("Bucket");
        useBucket.setOnAction(e -> {
            toolType = ToolType.BUCKET;
            System.out.println("tool type: " + toolType);
        });
        useBucket.setLayoutX(400);
        useBucket.setLayoutY(0);

        pane.getChildren().addAll(useBucket);

        // 文字工具
        Button addText = new Button("Text");
        addText.setOnAction(e -> {
           toolType = ToolType.TEXT;
        });
        addText.setLayoutX(340);
        addText.setLayoutY(0);
        pane.getChildren().add(addText);

        // 模式切换按钮
        btn.setOnAction(event -> {
            if (btn.getText().equals("switch to edit mode")) {
                editMode();
            } else if (btn.getText().equals("switch to draw mode")) {
                drawMode();
            }
        });

        // 字体选择器
        List<String> fontFamilies = Font.getFamilies();  // 获取系统所有字体名称
        fontFamilySelector = new ComboBox<>(FXCollections.observableList(fontFamilies));
        fontFamilySelector.setLayoutX(0);
        fontFamilySelector.setLayoutY(100);
        fontFamilySelector.getStyleClass().add("combo-box");
        fontFamilySelector.setValue("Arial");

        fontFamilySelector.valueProperty().addListener(event -> changeFont());
        pane.getChildren().add(fontFamilySelector);

        // 字号选择器
        fontSizeSpinner = new Spinner<>(1, 100, 10);
        fontSizeSpinner.setEditable(true);
        fontSizeSpinner.setPrefWidth(70);
        fontSizeSpinner.valueProperty().addListener(event -> changeFont());
        fontSizeSpinner.setLayoutX(120);
        fontSizeSpinner.setLayoutY(100);
        pane.getChildren().add(fontSizeSpinner);

        Scene scene = new Scene(pane, 600, 600);
        scene.getStylesheets().add("style.css"); // 绑定样式文件
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void changeFont() {
        String fontFamily = fontFamilySelector.getValue();
        int fontSize = fontSizeSpinner.getValue();
        Font font = Font.font(fontFamily, FontWeight.NORMAL, FontPosture.REGULAR, fontSize);
        Color color = cp.getValue();
        if (selectedText != null) {
            selectedText.setFont(font);
            if (mode.equals("edit"))
            selectedText.setFill(color);
        }
    }

    public void editMode() {
        mode = "edit";
        btn.setText("switch to draw mode");

        pane.setOnMousePressed(e -> {});
        pane.setOnMouseDragged(e -> {});
        // 让画布中所有元素可移动
        for (Node n: pane.getChildren()) {
            if (n instanceof MovableResizableRectangle) {
                ((MovableResizableRectangle) n).editMode();
            }
            if (!(n instanceof Circle)) {  // 排除掉可变大小矩形的两个锚点(圆形)
                n.setOnMousePressed(e -> { // 元素点击事件：捕捉元素原始位置
                    oriX = e.getSceneX();
                    oriY = e.getSceneY();
                    oriTranslateX = ((Node) (e.getSource())).getTranslateX();
                    oriTranslateY = ((Node) (e.getSource())).getTranslateY();
                    System.out.println("click shape.");
                    // 油漆桶，填充形状
                    if (toolType == ToolType.BUCKET && (n instanceof Rectangle || n instanceof Circle)) {
                        ((Shape) n).setFill(strokeColor);
                    }
                    if (n instanceof Text) {
                        selectedText = (Text) n;
                    }
                });
                n.setOnMouseDragged(e -> {  // 鼠标拖动事件：将选中元素拖动到当前位置
                    double offsetX = e.getSceneX() - oriX;
                    double offsetY = e.getSceneY() - oriY;
                    double newTranslateX = oriTranslateX + offsetX;
                    double newTranslateY = oriTranslateY + offsetY;

                    ((Node) (e.getSource())).setTranslateX(newTranslateX);
                    ((Node) (e.getSource())).setTranslateY(newTranslateY);
                });
                // 鼠标在形状上的样式: 小手
                n.setOnMouseEntered(e -> {
                    ((Node) (e.getSource())).getParent().setCursor(Cursor.HAND);
                });
                // 鼠标在画板上的样式: 默认鼠标样式
                n.setOnMouseExited(e -> {
                    ((Node) (e.getSource())).getParent().setCursor(Cursor.DEFAULT);
                });
            }
        }
    }

    public void drawMode() {
        // 绘图模式，激活画板鼠标点击和拖动事件(进行绘图)，
        // 关闭画板中所有元素的鼠标点击和拖动事件(所有元素不可拖动和改变大小)
        mode = "draw";
        btn.setText("switch to edit mode");

        // 激活画板鼠标点击事件
        pane.setOnMousePressed(e -> {
            x1 = e.getX();
            y1 = e.getY();
            System.out.println("(" + x1 + "," + x2 + ")");
            double sceneX1 = e.getSceneX();
            double sceneX2 = e.getSceneY();
            System.out.println("(" + sceneX1 + "," + sceneX2 + ")");
            // 画笔，自由绘制
            if (toolType == ToolType.PEN) {
                freeDraw = new Path();  // 新建自由绘制对象
                freeDraw.getElements().add(new MoveTo(x1, y1));  // 设定自由绘制起点
                freeDraw.setStrokeLineJoin(StrokeLineJoin.ROUND);
                pane.getChildren().add(freeDraw);  // 将自由绘制对象添加至画板中
            } else if (toolType == ToolType.RECTANGLE) {
                // 矩形子类
                rectangle = new MovableResizableRectangle(x1, y1, 0, 0);
                pane.getChildren().add(rectangle);

            } else if (toolType == ToolType.TEXT) {
                // 插入文字
                textInput = new TextField(); // 新建空的textField用于接收用户输入
                textInput.setLayoutX(sceneX1);  // 设置textField位置为当前鼠标点击位置
                textInput.setLayoutY(sceneX2);
                pane.getChildren().add(textInput);  // 向画板中添加textField
                textInput.setOnKeyPressed(keyEvent -> {  // 捕捉textField按键盘回车事件
                    if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                        String text = textInput.getText();  // 获取用户输入
                        textElement = new Text(textInput.getLayoutX(), textInput.getLayoutY(), text);  // 新建text对象
//                        textElement.setFont(new Font(20));
                        textElement.getStyleClass().add("rec");  // 设置样式
                        pane.getChildren().add(textElement);  // 向画板中添加Text
                        pane.getChildren().remove(textInput);  // 从画板中移除TextField
                    }
                });
            }
        });

        // 激活画板鼠标拖动事件
        pane.setOnMouseDragged(e -> {
            x2 = e.getX();
            y2 = e.getY();
            if (toolType == ToolType.PEN) {
                freeDraw.getElements().add(new LineTo(x2, y2));  // 向自由绘制对象中添加元素(线段)
                // 设置自由绘制对象样式(CSS), 画笔粗细和颜色
//                freeDraw.setStyle(String.format("-fx-stroke-width: %d; -fx-stroke: %s", strokeWidth, strokeColor));
                freeDraw.setStroke(strokeColor);  // 设置画笔颜色
                freeDraw.setStrokeWidth(strokeWidth); // 设置画笔粗细
                freeDraw.setId("shape");
            } else if (toolType == ToolType.RECTANGLE) {
                // rectangle
                double width = Math.abs(x1 - x2);
                double height = Math.abs(y1 - y2);
                // 矩形或矩形子类
                rectangle.setX(Math.min(x1, x2));
                rectangle.setY(Math.min(y1, y2));
                rectangle.setWidth(width);
                rectangle.setHeight(height);
//                rectangle.setStyle("-fx-fill: null; -fx-stroke: black; -fx-stroke-width: 3");
                rectangle.setFill(Color.rgb(0, 0, 0, 1d / 255d));  // 几乎透明的填充
                rectangle.setStroke(strokeColor);  // 边缘颜色
                rectangle.setStrokeWidth(strokeWidth);  // 边缘宽度
//                rectangle.setId("shape"); // enable hover effect, select by id
                rectangle.getStyleClass().add("rec");  // enable hover effect, select by class
            }
        });

        // 关闭画板中所有元素的鼠标点击和拖动事件(所有元素不可拖动和改变大小)
        for (Node n: pane.getChildren()) {
            if (n instanceof MovableResizableRectangle) {
                ((MovableResizableRectangle) n).drawMode();
            } else {
                if (n instanceof Circle || n instanceof Rectangle) {
                    n.setOnMousePressed(e -> {
                        if (toolType == ToolType.BUCKET) {
                            ((Shape) n).setFill(strokeColor);
                        }
                    });
                    n.setOnMouseDragged(e -> {
                    });
                } else {
                    n.setOnMousePressed(e -> {
                    });
                    n.setOnMouseDragged(e -> {
                    });
                }
            }

        }

        pane.setOnMouseReleased(e -> {
        });
    }
}
