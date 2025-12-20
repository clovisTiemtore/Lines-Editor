package ensisa.lines.commands;

import ensisa.lines.MainController;
import ensisa.lines.model.StraightLine;

import java.util.HashMap;
import java.util.Map;

public class MoveCommand implements UndoableCommand {

    private final MainController mainController;
    private final Map<StraightLine, double[]> before;
    private final Map<StraightLine, double[]> after;

    public MoveCommand(MainController mainController,
                       Map<StraightLine, double[]> before,
                       Map<StraightLine, double[]> after) {
        this.mainController = mainController;
        this.before = before;
        this.after = after;
    }

    @Override
    public void execute() {
        apply(after);
    }

    @Override
    public void undo() {
        apply(before);
    }

    private void apply(Map<StraightLine, double[]> data) {
        for (var entry : data.entrySet()) {
            StraightLine line = entry.getKey();
            double[] p = entry.getValue();
            line.setStartX(p[0]);
            line.setStartY(p[1]);
            line.setEndX(p[2]);
            line.setEndY(p[3]);
        }
    }
}
