package ensisa.lines.tools;

import ensisa.lines.MainController;
import ensisa.lines.model.StraightLine;
import javafx.scene.input.MouseEvent;

public class SelectTool implements Tool {

    private final MainController controller;

    private StraightLine draggedLine = null;
    private double offsetX;
    private double offsetY;

    public SelectTool(MainController controller) {
        this.controller = controller;
    }

    @Override
    public void mousePressed(MouseEvent e) {

        StraightLine line = controller.getDocument().findLineAt(e.getX(), e.getY());

        if (line != null) {
            controller.deselectAll();
            controller.getSelectedLines().add(line);

            draggedLine = line;
            offsetX = e.getX();
            offsetY = e.getY();
        } else {
            controller.deselectAll();
            draggedLine = null;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (draggedLine != null) {
            double dx = e.getX() - offsetX;
            double dy = e.getY() - offsetY;

            draggedLine.setStartX(draggedLine.getStartX() + dx);
            draggedLine.setStartY(draggedLine.getStartY() + dy);
            draggedLine.setEndX(draggedLine.getEndX() + dx);
            draggedLine.setEndY(draggedLine.getEndY() + dy);

            offsetX = e.getX();
            offsetY = e.getY();

            controller.getSelectedLines().forEach(controller::updateSelectionRect);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        draggedLine = null;
    }

    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
