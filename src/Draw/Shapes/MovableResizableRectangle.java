package Shapes;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.Arrays;

public class MovableResizableRectangle extends Rectangle {
    private Circle resizeHandleNW, resizeHandleSE;
    private double handleRadius = 5;

    public MovableResizableRectangle(double x, double y, double width, double height) {
        super(x, y, width, height);

        // top left resize handle:
        resizeHandleNW = new Circle(handleRadius);
        resizeHandleNW.setFill(Color.WHITE);
        resizeHandleNW.setStroke(Color.BLACK);
        resizeHandleNW.setStrokeWidth(2);
        // bind to top left corner of Rectangle:
//        resizeHandleNW.centerXProperty().bind(this.xProperty());
//        resizeHandleNW.centerYProperty().bind(this.yProperty());

        resizeHandleNW.translateXProperty().bind(this.translateXProperty());
        resizeHandleNW.translateYProperty().bind(this.translateYProperty());

        // bottom right resize handle:
        resizeHandleSE = new Circle(handleRadius);
        resizeHandleSE.setFill(Color.WHITE);
        resizeHandleSE.setStroke(Color.BLACK);
        resizeHandleSE.setStrokeWidth(2);
        // bind to bottom right corner of Rectangle:
//        resizeHandleSE.centerXProperty().bind(this.xProperty().add(this.widthProperty()));
//        resizeHandleSE.centerYProperty().bind(this.yProperty().add(this.heightProperty()));

        resizeHandleSE.translateXProperty().bind(this.translateXProperty());
        resizeHandleSE.translateYProperty().bind(this.translateYProperty());

        // force circles to live in same parent as rectangle:
        this.parentProperty().addListener((obs, oldParent, newParent) -> {
            for (Circle c : Arrays.asList(resizeHandleNW, resizeHandleSE)) {
                Pane currentParent = (Pane)c.getParent();
                if (currentParent != null) {
                    currentParent.getChildren().remove(c);
                }
                ((Pane)newParent).getChildren().add(c);
            }
        });

        // resize handles are invisible by default
        hideResizeHandle();
    }

    public void showResizeHandle() {
        resizeHandleNW.setVisible(true);
        resizeHandleSE.setVisible(true);
    }

    public void hideResizeHandle() {
        resizeHandleNW.setVisible(false);
        resizeHandleSE.setVisible(false);
    }

    public void editMode() {
        showResizeHandle();

        this.setDisable(false);
        resizeHandleNW.setDisable(false);
        resizeHandleSE.setDisable(false);

        System.out.println("Rec edit mode.");

        Wrapper<Point2D> mouseLocation = new Wrapper<>();

        setUpDragging(resizeHandleNW, mouseLocation);
        setUpDragging(resizeHandleSE, mouseLocation);

        resizeHandleNW.setOnMouseDragged(event -> {
            if (mouseLocation.value != null) {
                double deltaX = event.getSceneX() - mouseLocation.value.getX();
                double deltaY = event.getSceneY() - mouseLocation.value.getY();
                double newX = this.getX() + deltaX ;
                if (newX >= handleRadius
                        && newX <= this.getX() + this.getWidth() - handleRadius) {
                    this.setX(newX);
                    this.setWidth(this.getWidth() - deltaX);
                }
                double newY = this.getY() + deltaY ;
                if (newY >= handleRadius
                        && newY <= this.getY() + this.getHeight() - handleRadius) {
                    this.setY(newY);
                    this.setHeight(this.getHeight() - deltaY);
                }
                mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
            }
        });

        resizeHandleSE.setOnMouseDragged(event -> {
            if (mouseLocation.value != null) {
                double deltaX = event.getSceneX() - mouseLocation.value.getX();
                double deltaY = event.getSceneY() - mouseLocation.value.getY();
                double newMaxX = this.getX() + this.getWidth() + deltaX ;
                if (newMaxX >= this.getX()
                        && newMaxX <= this.getParent().getBoundsInLocal().getWidth() - handleRadius) {
                    this.setWidth(this.getWidth() + deltaX);
                }
                double newMaxY = this.getY() + this.getHeight() + deltaY ;
                if (newMaxY >= this.getY()
                        && newMaxY <= this.getParent().getBoundsInLocal().getHeight() - handleRadius) {
                    this.setHeight(this.getHeight() + deltaY);
                }
                mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
            }
        });
    }

    public void drawMode() {
        hideResizeHandle();

        this.setDisable(true);
        resizeHandleNW.setDisable(true);
        resizeHandleSE.setDisable(true);
    }

    private void setUpDragging(Circle circle, Wrapper<Point2D> mouseLocation) {
        circle.setOnMouseEntered(event -> {
            circle.getParent().setCursor(Cursor.NW_RESIZE);
        });
        circle.setOnDragDetected(event -> {

            mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
        });

        circle.setOnMouseExited(event -> {
            circle.getParent().setCursor(Cursor.DEFAULT);
        });

        circle.setOnMouseReleased(event -> {
//            circle.getParent().setCursor(Cursor.DEFAULT);
            mouseLocation.value = null ;
        });
    }

    static class Wrapper<T> { T value ; }
}
