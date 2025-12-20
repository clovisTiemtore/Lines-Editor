package ensisa.lines;

import ensisa.lines.model.StraightLine;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Map;

public class LinesEditor {

    private final Pane editorPane;

    private final Map<StraightLine, Line> lines = new HashMap<>();

    private final Map<StraightLine, Rectangle> selectionRects = new HashMap<>();

    final Map<StraightLine, Rectangle> startHandles = new HashMap<>();
    final Map<StraightLine, Rectangle> endHandles = new HashMap<>();

    private final MainController controller;

    public LinesEditor(Pane editorPane, MainController controller) {
        this.editorPane = editorPane;
        this.controller = controller;
    }

    private void bind(Line line, StraightLine straightLine) {
        line.startXProperty().bind(straightLine.startXProperty());
        line.startYProperty().bind(straightLine.startYProperty());
        line.endXProperty().bind(straightLine.endXProperty());
        line.endYProperty().bind(straightLine.endYProperty());
        line.strokeWidthProperty().bind(straightLine.strokeWidthProperty());
        line.strokeProperty().bind(straightLine.colorProperty());
    }

    public void createLine(StraightLine straightLine) {
        Line line = new Line();
        lines.put(straightLine, line);
        bind(line, straightLine);
        editorPane.getChildren().add(line);
    }

    public void removeLine(StraightLine straightLine) {
        Line line = lines.remove(straightLine);
        editorPane.getChildren().remove(line);

        Rectangle r1 = startHandles.remove(straightLine);
        Rectangle r2 = endHandles.remove(straightLine);
        Rectangle rect = selectionRects.remove(straightLine);

        editorPane.getChildren().removeAll(r1, r2, rect);
    }

    public void select(StraightLine line) {

        Rectangle rect = new Rectangle();
        rect.setStroke(javafx.scene.paint.Color.BLUE);
        rect.setFill(javafx.scene.paint.Color.TRANSPARENT);
        rect.setStrokeWidth(1.0);
        rect.getStrokeDashArray().addAll(4.0, 4.0);

        selectionRects.put(line, rect);
        editorPane.getChildren().add(rect);

        Rectangle start = createHandle();
        Rectangle end = createHandle();

        startHandles.put(line, start);
        endHandles.put(line, end);

        editorPane.getChildren().addAll(start, end);

        updateSelectionRect(line);
        updateHandles(line);
    }

    public void deselect(StraightLine line) {
        Rectangle rect = selectionRects.remove(line);
        Rectangle start = startHandles.remove(line);
        Rectangle end = endHandles.remove(line);

        editorPane.getChildren().removeAll(rect, start, end);
    }

    private Rectangle createHandle() {
        Rectangle r = new Rectangle(8, 8);
        r.setFill(javafx.scene.paint.Color.WHITE);
        r.setStroke(javafx.scene.paint.Color.BLACK);

        r.setOnMousePressed(e -> controller.handlePressed(r, e));
        r.setOnMouseDragged(e -> controller.handleDragged(r, e));
        r.setOnMouseReleased(e -> controller.handleReleased());

        return r;
    }

    public void updateSelectionRect(StraightLine line) {
        Rectangle rect = selectionRects.get(line);
        if (rect == null) return;

        double x1 = line.getStartX();
        double y1 = line.getStartY();
        double x2 = line.getEndX();
        double y2 = line.getEndY();

        rect.setX(Math.min(x1, x2) - 5);
        rect.setY(Math.min(y1, y2) - 5);
        rect.setWidth(Math.abs(x1 - x2) + 10);
        rect.setHeight(Math.abs(y1 - y2) + 10);

        updateHandles(line);
    }

    private void updateHandles(StraightLine line) {
        Rectangle start = startHandles.get(line);
        Rectangle end = endHandles.get(line);

        if (start == null || end == null) return;

        start.setX(line.getStartX() - 4);
        start.setY(line.getStartY() - 4);

        end.setX(line.getEndX() - 4);
        end.setY(line.getEndY() - 4);
    }
}
