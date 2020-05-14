import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import javax.tools.Tool;

public class BasicDrawWithUpdate extends Application {
    private Canvas layer = new Canvas();
    private double mouse_pressed_x;
    private double mouse_pressed_y;
    private double height;
    private double width;
    private ToolType toolType = ToolType.LINE;

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();

        Canvas canvas = new Canvas(400, 400); // 画布

        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();  // 画笔

        Button toggle = new Button("toggle");

        toggle.setOnAction(event -> {
            if (toolType == ToolType.LINE)
                toolType = ToolType.RECTANGLE;
            else if (toolType == ToolType.RECTANGLE)
                toolType = ToolType.LINE;
            System.out.println(toolType);
        });

        canvas.setOnMousePressed(event-> {
            // 每次绘图都有一个新的图层
            Canvas newLayer = new Canvas(400, 400);  // 新的图层
            GraphicsContext context = newLayer.getGraphicsContext2D();  // 新图层的画笔

            layer = newLayer; // 新的图层
            root.getChildren().add(0, newLayer);
            mouse_pressed_x = event.getSceneX();
            mouse_pressed_y = event.getSceneY();
        });

        canvas.setOnMouseDragged(event -> {
            // 捕捉鼠标拖动事件
            double current_x = event.getSceneX();
            double current_y = event.getSceneY();
            double upper_left_x = Math.min(mouse_pressed_x, current_x);
            double upper_left_y = Math.min(mouse_pressed_y, current_y);
            height = Math.abs(current_y - mouse_pressed_y);
            width = Math.abs(current_x - mouse_pressed_x);

            GraphicsContext context = layer.getGraphicsContext2D();
            context.clearRect(0, 0, layer.getWidth(), layer.getHeight()); // 清空画布

            // 画一条直线(从刚才点鼠标的位置到当前位置)
//            context.strokeLine(mouse_pressed_x, mouse_pressed_y, event.getSceneX(), event.getSceneY());
//            context.strokeRect(upper_left_x, upper_left_y, width, height);

            if (toolType == ToolType.LINE)
                context.strokeLine(mouse_pressed_x, mouse_pressed_y, event.getSceneX(), event.getSceneY());
            else if (toolType == ToolType.RECTANGLE)
                context.strokeRect(upper_left_x, upper_left_y, width, height);
        });

        root.getChildren().addAll(canvas, toggle);
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
