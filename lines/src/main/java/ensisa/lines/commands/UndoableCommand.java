package ensisa.lines.commands;

public interface UndoableCommand extends Command {
    void undo();
    default void redo() {
        execute();
    }

}
