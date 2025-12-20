package ensisa.lines.model;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

public class StraightLine {
    private final DoubleProperty startX;
    private final DoubleProperty startY;
    private final DoubleProperty endX;
    private final DoubleProperty endY;
    private final DoubleProperty strokeWidth;
    private final ObjectProperty<Color> color;

    public StraightLine() {
        startX = new SimpleDoubleProperty(0);
        startY = new SimpleDoubleProperty(0);
        endX = new SimpleDoubleProperty(0);
        endY = new SimpleDoubleProperty(0);
        strokeWidth = new SimpleDoubleProperty(2.0);
        color = new SimpleObjectProperty<>(Color.BLACK);
    }

    public double getStartX() {
        return startX.get();
    }

    public DoubleProperty startXProperty() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX.set(startX);
    }
    public double getStartY() {
        return startY.get();
    }
    public DoubleProperty startYProperty() {
        return startY;
    }
    public void setStartY(double startY) {
        this.startY.set(startY);
    }
    public double getEndX() {
        return endX.get();
    }
    public DoubleProperty endXProperty() {
        return endX;
    }
    public void setEndX(double endX) {
        this.endX.set(endX);
    }
    public double getEndY() {
        return endY.get();
    }
    public DoubleProperty endYProperty() {
        return endY;
    }
    public void setEndY(double endY) {
        this.endY.set(endY);
    }
    public double getStrokeWidth() {
        return strokeWidth.get();
    }
    public DoubleProperty strokeWidthProperty() {
        return strokeWidth;
    }
    public void setStrokeWidth(double strokeWidth) {
        this.strokeWidth.set(strokeWidth);
    }
    public Color getColor() {
        return color.get();
    }
    public ObjectProperty<Color> colorProperty() {
        return color;
    }
    public void setColor(Color color) {
        this.color.set(color);
    }
}