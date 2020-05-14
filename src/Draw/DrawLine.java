import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class DrawLine extends Application {
    private Canvas canvas;  // The canvas on which everything is drawn.

    private GraphicsContext g;  // For drawing on the canvas.

    private double start_x;
    private double start_y;
    private double end_x;
    private double end_y;

    @Override
    public void start(Stage stage) {
        canvas = new Canvas(600,400);  // like a paper
        g = canvas.getGraphicsContext2D();  // like a pen

//         用代码绘制直线
//        g.strokeLine(100, 200, 300, 400);

        // 用鼠标捕捉两个点用作直线绘制的起点和终点
        canvas.setOnMousePressed(event -> {
            // 鼠标点击事件
            start_x = event.getX();
            start_y = event.getY();
        });

        canvas.setOnMouseReleased(event -> {
            // 鼠标释放事件
            end_x = event.getX();
            end_y = event.getY();
            g.strokeLine(start_x, start_y, end_x, end_y);
        });

        Pane root = new Pane(canvas);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Simple Paint");
        stage.show();
    }
}
