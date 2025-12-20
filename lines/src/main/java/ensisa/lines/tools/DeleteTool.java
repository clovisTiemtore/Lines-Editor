package ensisa.lines.tools;

import ensisa.lines.MainController;
import ensisa.lines.model.StraightLine;
import javafx.scene.input.MouseEvent;

public class DeleteTool implements Tool {
    private final MainController mainController;

    public DeleteTool(MainController controller) {
        this.mainController = controller;
    }

    @Override
    public void mousePressed(MouseEvent event) {
        for (StraightLine line : mainController.getDocument().getLines()) {
            if (isNearLine(line, event.getX(), event.getY())) {
                mainController.getDocument().getLines().remove(line);
                System.out.println("Ligne supprimée !");
                break;
            }
        }
    }

    private boolean isNearLine(StraightLine line, double x, double y) {
        double dx = line.getEndX() - line.getStartX();
        double dy = line.getEndY() - line.getStartY();
        double lengthSquared = dx*dx + dy*dy;
        if (lengthSquared == 0) return false;

        double t = ((x - line.getStartX()) * dx + (y - line.getStartY()) * dy) / lengthSquared;
        if (t < 0 || t > 1) return false;

        double projX = line.getStartX() + t * dx;
        double projY = line.getStartY() + t * dy;
        double dist = Math.hypot(x - projX, y - projY);

        return dist < 5;
    }
}