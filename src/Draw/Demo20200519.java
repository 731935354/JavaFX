import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import java.util.Arrays;

public class Demo20200519 extends Application {
    private double x1, y1, x2, y2;
    private Path freeDraw;
    private Rectangle rectangle;
    private double oriX, oriY, oriTranslateX, oriTranslateY;
    private Pane pane;
    private Button btn, btnBack;
    private Label strokWidthLabel;
    private ToolType toolType;
    private double strokeWidth = 1;
    private Color strokeColor = Color.BLACK;

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
        btnBack.setLayoutX(400);
        btnBack.setLayoutY(0);
        pane.getChildren().add(btnBack);


        // 模式切换按钮
        btn = new Button("switch to edit mode");
        pane.getChildren().add(btn);


        ColorPicker cp = new ColorPicker();
        Slider slider = new Slider();

        cp.setValue(Color.BLACK);
        cp.setOnAction(e -> {
            strokeColor = cp.getValue();
            System.out.println("Color: " + strokeColor.toString());
        });
        cp.setLayoutX(0);
        cp.setLayoutY(50);
        pane.getChildren().add(cp);

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
        usePen.setLayoutX(300);
        usePen.setLayoutY(0);

        pane.getChildren().addAll(usePen, drawRec);

        // 模式切换按钮
        btn.setOnAction(event -> {
            if (btn.getText().equals("switch to edit mode")) {
                editMode();
            } else if (btn.getText().equals("switch to draw mode")) {
                drawMode();
            }
        });

        Scene scene = new Scene(pane, 600, 600);
        scene.getStylesheets().add("style.css"); // 绑定样式文件
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void editMode() {
        btn.setText("switch to draw mode");

        pane.setOnMousePressed(e -> {});
        pane.setOnMouseDragged(e -> {});
        // 让画布中所有元素可移动
        for (Node n: pane.getChildren()) {
            n.setOnMousePressed(e -> { // 元素点击事件：捕捉元素原始位置
                oriX = e.getSceneX();
                oriY = e.getSceneY();
                oriTranslateX = ((Node) (e.getSource())).getTranslateX();
                oriTranslateY = ((Node) (e.getSource())).getTranslateY();

//                for (Node n2 : pane.getChildren()) {
//                    if (n2 instanceof Rectangle)
//                        n2.setStyle("-fx-stroke-width: 3; -fx-fill: white; -fx-stroke: black;");
//                    else if (n2 instanceof Path)
//                        n2.setStyle("-fx-stroke-width: 3;");
//                    System.out.println(n2.getClass());
//                    if (n2 == e.getSource())
//                        if (n2 instanceof Rectangle)
//                            n2.setStyle("-fx-stroke-width: 3; -fx-fill: white; -fx-stroke: black; -fx-effect: dropshadow(gaussian,#2c92ff,10,0,0,0)");
//                        else if (n2 instanceof Path)
//                            n2.setStyle("-fx-stroke-width: 3; -fx-effect: dropshadow(gaussian,#2c92ff,10,0,0,0)");
//                }

            });
            n.setOnMouseDragged(e -> {  // 鼠标拖动事件：将选中元素拖动到当前位置
                double offsetX = e.getSceneX() - oriX;
                double offsetY = e.getSceneY() - oriY;
                double newTranslateX = oriTranslateX + offsetX;
                double newTranslateY = oriTranslateY + offsetY;

                ((Node) (e.getSource())).setTranslateX(newTranslateX);
                ((Node) (e.getSource())).setTranslateY(newTranslateY);
            });
        }
    }

    public void drawMode() {
        // 绘图模式，激活画板鼠标点击和拖动事件(进行绘图)，
        // 关闭画板中所有元素的鼠标点击和拖动事件(所有元素不可拖动和改变大小)

        btn.setText("switch to edit mode");

        // 激活画板鼠标点击事件
        pane.setOnMousePressed(e -> {
            x1 = e.getX();
            y1 = e.getY();
            // 画笔，自由绘制
            if (toolType == ToolType.PEN) {
                freeDraw = new Path();  // 新建自由绘制对象
                freeDraw.getElements().add(new MoveTo(x1, y1));  // 设定自由绘制起点
                freeDraw.setStrokeLineJoin(StrokeLineJoin.ROUND);
                pane.getChildren().add(freeDraw);  // 将自由绘制对象添加至画板中
            } else if (toolType == ToolType.RECTANGLE) {
                rectangle = new Rectangle(x1, y1, 0, 0);
                pane.getChildren().add(rectangle);
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
                rectangle.setX(Math.min(x1, x2));
                rectangle.setY(Math.min(y1, y2));
                rectangle.setWidth(width);
                rectangle.setHeight(height);
//                rectangle.setStyle("-fx-fill: null; -fx-stroke: black; -fx-stroke-width: 3");
                rectangle.setFill(null);  // 无内部填充
                rectangle.setStroke(strokeColor);  // 边缘颜色
                rectangle.setStrokeWidth(strokeWidth);  // 边缘宽度
//                rectangle.setId("shape"); // enable hover effect, select by id
                rectangle.getStyleClass().add("rec");  // enable hover effect, select by class
            }
        });

        // 关闭画板中所有元素的鼠标点击和拖动事件(所有元素不可拖动和改变大小)
        for (Node n: pane.getChildren()) {
            n.setOnMousePressed(e -> {});
            n.setOnMouseDragged(e -> {});
        }

        pane.setOnMouseReleased(e -> {
//            for (Node n: pane.getChildren())
//                System.out.println(n.getClass());
//            if (toolType == ToolType.RECTANGLE) {
//                // 添加用来变换大小的激活点
//                double handleR = 5;
//                // 左上角激活点
//                Circle topLeftResizeHandle = new Circle(handleR);
//                topLeftResizeHandle.setFill(Color.WHITE);  // 填充色
//                topLeftResizeHandle.setStroke(Color.BLACK);  // 边缘颜色
//                topLeftResizeHandle.setStrokeWidth(2);  // 边缘宽度
//                // 与矩形左上角绑定
//                topLeftResizeHandle.centerXProperty().bind(rectangle.xProperty());
//                topLeftResizeHandle.centerYProperty().bind(rectangle.yProperty());
//
//                pane.getChildren().add(topLeftResizeHandle);
//
//                // force circles to live in same parent as rectangle:
//                rectangle.parentProperty().addListener((obs, oldParent, newParent) -> {
//                    for (Circle c : Arrays.asList(topLeftResizeHandle)) {
//                        Pane currentParent = (Pane) c.getParent();
//                        if (currentParent != null) {
//                            currentParent.getChildren().remove(c);
//                        }
//                        ((Pane) newParent).getChildren().add(c);
//                    }
//                });
//
//                topLeftResizeHandle.setOnMousePressed(event -> {
//                    x1 = event.getSceneX();
//                    y1 = event.getSceneY();
//                });
//
//                topLeftResizeHandle.setOnMouseDragged(event -> {
//                    //                    if (mouseLocation.value != null) {
//                    double deltaX = event.getX() - x1;
//                    double deltaY = event.getY() - y1;
//                    double newX = rectangle.getX() + deltaX;
//                    System.out.println(deltaX + "," + deltaY + "," + newX);
//                    if (newX >= handleR
//                            && newX <= rectangle.getX() + rectangle.getWidth() - handleR) {
//                        System.out.println("a");
//                        rectangle.setX(newX);
//                        rectangle.setWidth(rectangle.getWidth() - deltaX);
//                    }
//                    double newY = rectangle.getY() + deltaY;
//                    if (newY >= handleR
//                            && newY <= rectangle.getY() + rectangle.getHeight() - handleR) {
//                        System.out.println("b");
//                        rectangle.setY(newY);
//                        rectangle.setHeight(rectangle.getHeight() - deltaY);
//                    }
//                    x1 = event.getX();
//                    y1 = event.getSceneY();
//                    //                        mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
//                    //                    }
//                });
//                // 右下角激活点
//            }
        });
    }
}
