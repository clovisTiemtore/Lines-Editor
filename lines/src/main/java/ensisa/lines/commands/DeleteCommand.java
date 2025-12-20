package ensisa.lines.commands;

import ensisa.lines.MainController;
import ensisa.lines.model.StraightLine;
import java.util.*;

public class DeleteCommand implements UndoableCommand {

    private final MainController mainController;
    private final List<StraightLine> savedLines;
    private final Set<StraightLine> savedSelectedLines;

    public DeleteCommand(MainController mainController) {
        this.mainController = mainController;
        savedLines = new ArrayList<>(mainController.getDocument().getLines());
        savedSelectedLines = new HashSet<>(mainController.getSelectedLines());
    }

    @Override
    public void execute() {
        mainController.deselectAll();
        mainController.getDocument().getLines().removeAll(savedSelectedLines);
    }

    @Override
    public void undo() {
        mainController.deselectAll();
        mainController.getDocument().getLines().clear();
        mainController.getDocument().getLines().addAll(savedLines);
        mainController.getSelectedLines().addAll(savedSelectedLines);
    }
}
