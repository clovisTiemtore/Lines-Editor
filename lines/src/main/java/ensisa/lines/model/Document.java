package ensisa.lines.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Document {
    private final ObservableList<StraightLine> lines;

    public Document() {
        lines = FXCollections.observableArrayList();
    }

    public ObservableList<StraightLine> getLines() {
        return lines;
    }

    public StraightLine findLineAt(double x, double y) {
        for (StraightLine line : lines) {
            if (isPointNearLine(x, y, line)) {
                return line;
            }
        }
        return null;
    }

    private boolean isPointNearLine(double x, double y, StraightLine line) {
        double x1 = line.getStartX();
        double y1 = line.getStartY();
        double x2 = line.getEndX();
        double y2 = line.getEndY();

        double dx = x2 - x1;
        double dy = y2 - y1;

        if (dx == 0 && dy == 0) {
            return Math.hypot(x - x1, y - y1) < 5;
        }

        double t = ((x - x1) * dx + (y - y1) * dy) / (dx * dx + dy * dy);
        t = Math.max(0, Math.min(1, t));

        double projX = x1 + t * dx;
        double projY = y1 + t * dy;

        double distance = Math.hypot(x - projX, y - projY);

        return distance < 5;
    }

}