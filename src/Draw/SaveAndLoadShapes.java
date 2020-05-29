import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.json.*;
import java.io.*;
import java.util.Iterator;
import java.util.List;

public class SaveAndLoadShapes extends Application {
    private Rectangle rec;
    private Path freeDraw;
    private Circle circle;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane pane = new Pane();

        // save shapes to json file
//         矩形
//        rec = new Rectangle(100, 100, 100, 100);
//        rec.setFill(Color.rgb(0, 0, 0, 1d / 255d));
//        rec.setStrokeWidth(3);
//        rec.setStroke(Color.BLACK);
//        pane.getChildren().add(rec);

        // 自由曲线
//        freeDraw = new Path();
//        freeDraw.getElements().add(new MoveTo(200, 300));
//        freeDraw.getElements().add(new LineTo(300, 400));
//        freeDraw.getElements().add(new LineTo(400, 420));
//        freeDraw.setStroke(Color.GREEN);
//        freeDraw.setStrokeWidth(4);
//        pane.getChildren().add(freeDraw);

        // 文字
//        Text text = new Text("Happy");
////        List<String> fontFamilies = Font.getFamilies();
//        Font font = Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 40.0);
//        text.setFill(Color.PURPLE);
//        text.setFont(font);
//        text.setX(400);
//        text.setY(500);
//        pane.getChildren().add(text);

//        saveShapes(pane, "shapes_rec.json");

        loadShapes(pane, "shapes_4.json");

        Scene scene = new Scene(pane, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void saveShapes(Pane pane, String savePath) {
        JSONArray shapesInfo = new JSONArray();  // 一个数组，每个元素都是一个形状的所有信息

        try {
            for (Node s: pane.getChildren()) {
                if (s instanceof Rectangle) {
                    // 转化节点类型
                    Rectangle rec = (Rectangle) s;
                    // 类比dictionary，新建空的dictionary
                    JSONObject recInfo = new JSONObject();

                    // 往dictionary里面添加key-value对
                    recInfo.put("shape", "rectangle");
                    recInfo.put("x", rec.getX());
                    recInfo.put("y", rec.getY());
                    recInfo.put("height", rec.getHeight());
                    recInfo.put("width", rec.getWidth());
                    recInfo.put("stroke", rec.getStroke());
                    recInfo.put("strokeWidth", rec.getStrokeWidth());
                    recInfo.put("fill", rec.getFill());

                    // 把dictionary加入到数组
                    shapesInfo.put(recInfo);
                } else if (s instanceof Path) {
                    Path freeDraw = (Path) s;
                    // 新建一个空的dictionary
                    JSONObject freeDrawInfo = new JSONObject();
                    // 形状的标识符
                    freeDrawInfo.put("shape", "freeDraw");

                    JSONArray elements = new JSONArray();
                    // 遍历path中的元素，MoveTo或者LineTo
                    for (PathElement e: freeDraw.getElements()) {
                        if (e instanceof MoveTo) {
                            MoveTo moveTo = (MoveTo) e;
                            JSONObject ele = new JSONObject();
                            ele.put("move-to", moveTo.getX() + "," + moveTo.getY());
                            elements.put(ele);
                        } else if (e instanceof LineTo) {
                            LineTo lineTo = (LineTo) e;
                            JSONObject ele = new JSONObject();
                            ele.put("line-to", lineTo.getX() + "," + lineTo.getY());
                            elements.put(ele);
                        }
                    }
                    freeDrawInfo.put("elements", elements);
                    freeDrawInfo.put("stroke", freeDraw.getStroke());
                    freeDrawInfo.put("strokeWidth", freeDraw.getStrokeWidth());

                    shapesInfo.put(freeDrawInfo);
                } else if (s instanceof Text) {
                    Text textElement = (Text) s;
                    Font font = textElement.getFont();
                    String fontFamily = font.getFamily();
                    double fontSize = font.getSize();
                    JSONObject textInfo = new JSONObject();
                    textInfo.put("shape", "text");
                    textInfo.put("fontFamily", fontFamily);
                    textInfo.put("fontSize", fontSize);
                    textInfo.put("content", textElement.getText());
                    textInfo.put("fill", textElement.getFill());
                    textInfo.put("x", textElement.getX());
                    textInfo.put("y", textElement.getY());
                    shapesInfo.put(textInfo);
                }
            }

            FileWriter outputStream = null;

            outputStream = new FileWriter(savePath, false);
            outputStream.write(shapesInfo.toString(4));
            outputStream.flush();
            outputStream.close();
        } catch (IOException | JSONException e) {
            System.out.println("Failed to save the shapes to " + savePath);
        }
    }

    private JSONArray readJsonArray(String jsonPath) {
        try {
            // json解析器
            JSONTokener tokener = new JSONTokener(new FileReader(jsonPath));
            return new JSONArray(tokener);
        } catch (FileNotFoundException e) {
            System.out.println("File cannot be found. Please confirm the name.");
        } catch (JSONException e) {
            System.out.println("Failed to process json file " + jsonPath);
        }
        return null;
    }

    private void loadShapes(Pane pane, String jsonPath) {
        // load shapes from json file
        JSONArray shapesInfo = readJsonArray(jsonPath);

        try {
            for (int i = 0; i < shapesInfo.length(); i++) {
                JSONObject sInfo = shapesInfo.getJSONObject(i);
                String shapeType = sInfo.getString("shape");
                // 重建矩形
                if (shapeType.equals("rectangle")) {
                    // 获取矩形属性
                    double x = sInfo.getDouble("x");
                    double y = sInfo.getDouble("y");
                    double height = sInfo.getDouble("height");
                    double width = sInfo.getDouble("width");
                    String fill = sInfo.getString("fill");
                    Color fillColor = Color.valueOf(fill);
                    double strokeWidth = sInfo.getDouble("strokeWidth");
                    String stroke = sInfo.getString("stroke");
                    Color strokeColor = Color.valueOf(stroke);

                    // 重建矩形
                    Rectangle rec = new Rectangle(x, y, height, width);
                    rec.setFill(fillColor);
                    rec.setStroke(strokeColor);
                    rec.setStrokeWidth(strokeWidth);
                    // 将重建的矩形添加到画板上
                    pane.getChildren().add(rec);

                } else if (shapeType.equals("circle")) {
                    System.out.println("not implemented yet.");
                } else if (shapeType.equals("text")) {
                    String content = sInfo.getString("content");
                    double x = sInfo.getDouble("x");
                    double y = sInfo.getDouble("y");
                    String fill = sInfo.getString("fill");
                    Text text = new Text(content);
                    String fontFamily = sInfo.getString("fontFamily");
                    double fontSize = sInfo.getDouble("fontSize");
                    Font font = Font.font(fontFamily, FontWeight.NORMAL, FontPosture.REGULAR, fontSize);
                    text.setFont(font);
                    text.setX(x);
                    text.setY(y);
                    text.setFill(Color.valueOf(fill));
                    pane.getChildren().add(text);
                } else if (shapeType.equals("freeDraw")) {  // 重建自由曲线
                    Path freeDraw = new Path();  // 新建空的自由曲线对象
                    // 获取自由曲线属性
                    // 获取子元素，包括MoveTo和LineTo
                    JSONArray elements = sInfo.getJSONArray("elements");
                    for (int j = 0; j < elements.length(); j++) {
                        JSONObject pathElement = elements.getJSONObject(j);
                        //
                        Iterator<String> keys = pathElement.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            if (key.equals("move-to")) {
                                String x_y = pathElement.getString("move-to");
                                String[] points = x_y.split(",");
                                double x = Double.parseDouble(points[0]);
                                double y = Double.parseDouble(points[1]);
                                freeDraw.getElements().add(new MoveTo(x, y));
                            } else if (key.equals("line-to")) {
                                String x_y = pathElement.getString("line-to");
                                String[] points = x_y.split(",");
                                double x = Double.parseDouble(points[0]);
                                double y = Double.parseDouble(points[1]);
                                freeDraw.getElements().add(new LineTo(x, y));
                            }
                        }
                    }
                    // 获取线条宽度
                    double strokeWidth = sInfo.getDouble("strokeWidth");
                    // 获取线条颜色
                    String stroke = sInfo.getString("stroke");

                    // 设置重建的自由曲线对象属性
                    freeDraw.setStrokeWidth(strokeWidth);
                    freeDraw.setStroke(Color.valueOf(stroke));
                    // 将自由曲线对象添加至画板
                    pane.getChildren().add(freeDraw);
                }
            }
        } catch (JSONException e) {
            System.out.println("Failed to load shapes");
        }
    }
}
