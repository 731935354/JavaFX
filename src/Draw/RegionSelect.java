import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.util.*;

public class RegionSelect extends Application {
    private double oriX, oriY, oriTranslateX, oriTranslateY;
    private double x1, y1, x2, y2;
    private HashMap<Shape, Double> deltaXs = new HashMap<>();  // 记录形状与鼠标点击位置的横坐标偏移量
    private HashMap<Shape, Double> deltaYs = new HashMap<>();  // 记录形状与鼠标点击位置的纵坐标偏移量
    private ArrayList<Shape> allShapes, selectedShapes;
    private Rectangle rectangle;
    private boolean onCreateSelectRegion = true;  // true: 正在创建选区，false: 选区创建结束，开始拖动选区内形状

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane pane = new Pane();

        // 新建矩形
        Rectangle rec = new Rectangle(100, 100, 20, 30);
        rec.setFill(null);
        rec.setStrokeWidth(3);
        rec.setStroke(Color.BLACK);

        // 新建圆形
        Circle circle = new Circle(200, 200, 40);
        circle.setFill(null);
        circle.setStrokeWidth(3);
        circle.setStroke(Color.BLACK);

        // 把圆形和矩形添加到画板中
        pane.getChildren().addAll(rec, circle);

        // 存储画板中目前所有的形状
        allShapes = new ArrayList<>();
        allShapes.add(rec);
        allShapes.add(circle);

        // 存储被选区选中的形状
        selectedShapes = new ArrayList<>();

        Button reselect = new Button("Reselect");
        reselect.setOnAction(e -> {
            selectedShapes = new ArrayList<>();
            pane.getChildren().remove(rectangle);
            onCreateSelectRegion = true;
        });
        pane.getChildren().add(reselect);

        // 画板鼠标点击事件
        pane.setOnMousePressed(e -> {
            // 绘制选区状态(矩形选区)
            if (onCreateSelectRegion) {
                x1 = e.getX();
                y1 = e.getY();
                // 真实的选区矩形
                rectangle = new Rectangle(x1, y1, 0, 0);
                pane.getChildren().add(rectangle);
            } else {
                // 选取绘制完毕
                // 点击画布时记录所有已选中形状的起始坐标与当前鼠标点击位置的相对偏移量
                for (Shape s: selectedShapes) {
                    double deltaX = 0;
                    double deltaY = 0;
                    if (s instanceof Circle) {  // 圆心与当前鼠标点击位置的偏移量
                        deltaX = ((Circle) s).getCenterX() - e.getX();
                        deltaY = ((Circle) s).getCenterY() - e.getY();

                    } else if (s instanceof Rectangle) {  // 矩形左上角与当前鼠标点击位置的偏移量
                        deltaX = ((Rectangle) s).getX() - e.getX();
                        deltaY = ((Rectangle) s).getY() - e.getY();
                    }
                    deltaXs.put(s, deltaX);  // 将x偏移量存储到相应dictionary中
                    deltaYs.put(s, deltaY);  // 将y偏移量存储到相应dictionary中
                }
                // 记录选区矩形偏移量(从而在拖动时让选取也跟随移动
                deltaXs.put(rectangle, rectangle.getX() - e.getX());
                deltaYs.put(rectangle, rectangle.getY() - e.getY());
            }
        });

        pane.setOnMouseDragged(e -> {
            if (onCreateSelectRegion) {  // 绘制选区状态
                x2 = e.getX();
                y2 = e.getY();

                double width = Math.abs(x1 - x2);
                double height = Math.abs(y1 - y2);
                rectangle.setX(Math.min(x1, x2));
                rectangle.setY(Math.min(y1, y2));
                rectangle.setWidth(width);
                rectangle.setHeight(height);
                rectangle.setStyle("-fx-stroke: black; -fx-stroke-width: 2; -fx-stroke-dash-array: 20 10");
                // 将选区矩形背景色设成"几乎透明"，可以捕捉事件
                rectangle.setFill(Color.rgb(0, 0, 0, 1d / 255d));
            }
        });

        // 画板鼠标释放事件
        pane.setOnMouseReleased(event -> {
            if (onCreateSelectRegion) { // 选区绘制状态
                onCreateSelectRegion = false; // 选区绘制结束

                // 计算选中的形状
                for (Shape s: allShapes) {
                    if (rectangle.intersects(s.getBoundsInParent())) {
                        selectedShapes.add(s);
                    }
                }
            }

            // 设置选区鼠标拖拽事件
            rectangle.setOnMouseDragged(e -> {
                // 同时移动选区中所有形状，以及选区本身
                for (Shape shape: selectedShapes) {
                    if (shape instanceof Rectangle) {
                        Rectangle cur_rec = ((Rectangle) shape);
                        cur_rec.setX(e.getX() + deltaXs.get(shape));
                        cur_rec.setY(e.getY() + deltaYs.get(shape));
                    } else if (shape instanceof Circle) {
                        Circle cur_cir = ((Circle) shape);
                        cur_cir.setCenterX(e.getX() + deltaXs.get(shape));
                        cur_cir.setCenterY(e.getY() + deltaYs.get(shape));
                    }
                }

                // 选区的矩形也跟着移动
                rectangle.setX(e.getX() + deltaXs.get(rectangle));
                rectangle.setY(e.getY() + deltaYs.get(rectangle));
            });
        });

        Scene scene = new Scene(pane,600, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
