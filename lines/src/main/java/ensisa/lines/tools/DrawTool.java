package ensisa.lines.tools;

import ensisa.lines.MainController;
import ensisa.lines.commands.DrawCommand;
import ensisa.lines.model.StraightLine;
import javafx.scene.input.MouseEvent;
import javafx.scene.Cursor;

public class DrawTool implements Tool {
    enum State { initial, drawing }
    private State state;
    private StraightLine currentLine;
    private final MainController mainController;

    public DrawTool(MainController controller) {
        state = State.initial;
        this.mainController = controller;
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (event.isPrimaryButtonDown()) {
            var command = new DrawCommand(
                    mainController.getDocument(),
                    event.getX(),
                    event.getY()
            );

            currentLine = command.straightLine;

            mainController.execute(command);

            state = State.drawing;
        }
    }


    @Override
    public void mouseDragged(MouseEvent event) {
        if (state == State.drawing && event.isPrimaryButtonDown()) {
            currentLine.setEndX(event.getX());
            currentLine.setEndY(event.getY());
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        state = State.initial;
    }

    @Override
    public void mouseEntered(MouseEvent event) {
        mainController.editorPane.setCursor(Cursor.CROSSHAIR);
    }

    @Override
    public void mouseExited(MouseEvent event) {
        mainController.editorPane.setCursor(Cursor.DEFAULT);
    }
}