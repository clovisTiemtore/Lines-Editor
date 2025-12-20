package ensisa.lines.commands;

import ensisa.lines.model.Document;
import ensisa.lines.model.StraightLine;

public class DrawCommand implements UndoableCommand {
    public StraightLine straightLine;
    private final Document document;

    public DrawCommand(Document document, double x, double y) {
        this.document = document;
        straightLine = new StraightLine();
        straightLine.setStartX(x);
        straightLine.setStartY(y);
        straightLine.setEndX(x);
        straightLine.setEndY(y);
    }

    @Override
    public void execute() {
        document.getLines().add(straightLine);
    }

    @Override
    public void undo() {
        document.getLines().remove(straightLine);
    }
}
