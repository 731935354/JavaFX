import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class ResizableRecAndCircle extends Application {
    Rectangle rec;
    Circle circle;
    double marginWidth = 2;
    private double mouse_pressed_x, mouse_pressed_y;
    private double rec_height, rec_width, rec_upper_left_x, rec_upper_left_y;
    private double circle_center_x, circle_center_y, circle_radius;
    private boolean resize_rec_top, resize_rec_bottom, resize_rec_left, resize_rec_right;
    private boolean resize_circle;

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();
        rec = new Rectangle(100, 100, 100, 100);
        rec.setFill(Color.rgb(0, 0, 0, 1d / 255d));
        rec.setStrokeWidth(3);
        rec.setStroke(Color.BLACK);
        pane.getChildren().add(rec);

        circle = new Circle(300, 300, 50);
        circle.setFill(Color.rgb(0, 0, 0, 1d / 255d));
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(3);
        pane.getChildren().add(circle);

        circle.setOnMouseMoved(e -> {
            // 鼠标移动到圆形边界，改变鼠标样式 -> 小手
            if (mouseOnCircleBorder(e.getX(), e.getY(), circle)) {
                circle.getParent().setCursor(Cursor.HAND);
            } else { // 在圆形内部，鼠标为默认样式
                circle.getParent().setCursor(Cursor.DEFAULT);
            }
        });

        circle.setOnMouseExited(e -> {
            // 鼠标在圆形之外的空白区域，指针为默认样式
            circle.getParent().setCursor(Cursor.DEFAULT);
        });

        circle.setOnMousePressed(e -> {
            // 如果点击圆形边缘，则记录圆形的属性，包括中心位置和半径，
            // 同时切换到"修改圆形大小"模式
            if (mouseOnCircleBorder(e.getX(), e.getY(), circle)) {
                mouse_pressed_x = e.getX();
                mouse_pressed_y = e.getY();
                circle_center_x = circle.getCenterX();
                circle_center_y = circle.getCenterY();
                circle_radius = circle.getRadius();
                resize_circle = true;
            }
        });

        // 圆形拖动事件
        circle.setOnMouseDragged(e -> {
            if (resize_circle) { // 如果处于修改大小状态
                // 当前鼠标位置
                double x = e.getX();
                double y = e.getY();
                // 拖拽开始瞬间鼠标点击时，鼠标与圆心的欧几里得距离
                double ori_dis = euclideanDistance(mouse_pressed_x, mouse_pressed_y, circle_center_x, circle_center_y);
                // 当前鼠标位置与圆心的欧几里得距离
                double current_dis = euclideanDistance(x, y, circle_center_x, circle_center_y);
                // 两个距离之差
                double dis_delta = current_dis - ori_dis;
                // 新的半径
                double new_radius = circle_radius + dis_delta;
                // 新的半径如果合理，则更新圆形的半径
                if (new_radius >= marginWidth)
                    circle.setRadius(new_radius);
            }
        });

        circle.setOnMouseReleased(e -> {
            // 关闭"修改圆形大小"模式
            resize_circle = false;
        });


        // 鼠标位于矩形边缘时，设置鼠标样式，提示用户可以调整大小
        rec.setOnMouseMoved(e -> {
            if (mouseOnRecTopMargin(e.getX(), e.getY(), rec)) { // 矩形上边界
                rec.getParent().setCursor(Cursor.V_RESIZE);
            } else if (mouseOnRecBottomMargin(e.getX(), e.getY(), rec)) {  // 矩形下边界
                rec.getParent().setCursor(Cursor.V_RESIZE);
            } else if (mouseOnRecLeftMargin(e.getX(), e.getY(), rec)) {  // 矩形左边界
                rec.getParent().setCursor(Cursor.H_RESIZE);
            } else if (mouseOnRecRightMargin(e.getX(), e.getY(), rec)) {  // 矩形右边界
                rec.getParent().setCursor(Cursor.H_RESIZE);
            } else {
                rec.getParent().setCursor(Cursor.DEFAULT);
            }
        });

        // 鼠标在矩形之外的区域，将鼠标设为默认样式
        rec.setOnMouseExited(e -> {
            rec.getParent().setCursor(Cursor.DEFAULT);
        });

        rec.setOnMousePressed(e -> {
            // 点击瞬间鼠标位置
            mouse_pressed_x = e.getSceneX();
            mouse_pressed_y = e.getSceneY();
            // 如果点击矩形边缘，则记录矩形的属性，包括左上角坐标，及矩形的高度和宽度
            if (mouseOnRecTopMargin(mouse_pressed_x, mouse_pressed_y, rec) ||
                    mouseOnRecBottomMargin(mouse_pressed_x, mouse_pressed_y, rec) ||
                    mouseOnRecLeftMargin(mouse_pressed_x, mouse_pressed_y, rec) ||
                    mouseOnRecRightMargin(mouse_pressed_x, mouse_pressed_y, rec)) {
                rec_height = rec.getHeight();
                rec_width = rec.getWidth();
                rec_upper_left_x = rec.getX();
                rec_upper_left_y = rec.getY();
            }
            // 根据点击的边缘设置相应状态
            if (mouseOnRecTopMargin(mouse_pressed_x, mouse_pressed_y, rec))
                resize_rec_top = true;
            else if (mouseOnRecBottomMargin(mouse_pressed_x, mouse_pressed_y, rec))
                resize_rec_bottom = true;
            else if (mouseOnRecLeftMargin(mouse_pressed_x, mouse_pressed_y, rec))
                resize_rec_left = true;
            else if (mouseOnRecRightMargin(mouse_pressed_x, mouse_pressed_y, rec))
                resize_rec_right = true;
        });

        rec.setOnMouseDragged(e -> {
            // 鼠标拖动的横纵坐标偏移量(与拖动起始位置相比)
            double delta_y = e.getSceneY() - mouse_pressed_y;
            double delta_x = e.getSceneX() - mouse_pressed_x;
            if (resize_rec_top) {  // 拖动顶部边界状态
                double new_height = rec_height - delta_y;
                if (new_height >= marginWidth) {
                    rec.setY(rec_upper_left_y + delta_y);
                    rec.setHeight(new_height);
                }
            } else if (resize_rec_bottom) {  // 拖动底部边界
                double new_height = rec_height + delta_y;
                if (new_height >= marginWidth)
                    rec.setHeight(new_height);
            } else if (resize_rec_left) {  // 拖动左侧边界
                double new_width = rec_width - delta_x;
                if (new_width >= marginWidth) {
                    rec.setX(rec_upper_left_x + delta_x);
                    rec.setWidth(new_width);
                }
            } else if (resize_rec_right) {  // 拖动右侧边界
                double new_width = rec_width + delta_x;
                if (new_width >= marginWidth) {
                    rec.setWidth(new_width);
                }
            }
        });

        // 拖动结束，关闭所有"改变大小"状态
        rec.setOnMouseReleased(e -> {
            resize_rec_top = false;
            resize_rec_bottom = false;
            resize_rec_left = false;
            resize_rec_right = false;
        });

        Scene scene = new Scene(pane,600, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // 鼠标位于矩形上边界
    private boolean mouseOnRecTopMargin(double mouse_x, double mouse_y, Rectangle rec) {
        boolean res = (mouse_x >= rec.getX() - marginWidth &&
                mouse_x <= rec.getX() + rec.getWidth() + marginWidth &&
                mouse_y >= rec.getY() - marginWidth &&
                mouse_y <= rec.getY() + marginWidth);
//        System.out.println(res);
        return res;
    }

    // 鼠标位于矩形下边界
    private boolean mouseOnRecBottomMargin(double mouse_x, double mouse_y, Rectangle rec) {
        boolean res = (mouse_x >= rec.getX() - marginWidth &&
                mouse_x <= rec.getX() + rec.getWidth() + marginWidth &&
                mouse_y >= rec.getY() + rec.getHeight() - marginWidth &&
                mouse_y <= rec.getY() + rec.getHeight() + marginWidth);
        return res;
    }

    // 鼠标位于矩形左边界
    private boolean mouseOnRecLeftMargin(double mouse_x, double mouse_y, Rectangle rec) {
        boolean res = (mouse_x >= rec.getX() - marginWidth &&
                mouse_x <= rec.getX() + marginWidth &&
                mouse_y >= rec.getY() - marginWidth &&
                mouse_y <= rec.getY() + rec.getHeight() + marginWidth);
        return res;
   }

    // 鼠标位于矩形右边界
    private boolean mouseOnRecRightMargin(double mouse_x, double mouse_y, Rectangle rec) {
        boolean res = (mouse_x >= rec.getX() + rec.getWidth() - marginWidth &&
                mouse_x <= rec.getX() + rec.getWidth() + marginWidth &&
                mouse_y >= rec.getY() - marginWidth &&
                mouse_y <= rec.getY() + rec.getHeight() + marginWidth);
        return res;
    }

    // 鼠标位于圆形边界
    private boolean mouseOnCircleBorder(double mouse_x, double mouse_y, Circle circle) {
        double circle_center_x = circle.getCenterX();
        double circle_center_y = circle.getCenterY();
        double distance = euclideanDistance(mouse_x, mouse_y, circle_center_x, circle_center_y);
        return distance >= circle.getRadius() - marginWidth;
    }

    private double euclideanDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
