package ensisa.lines;

import ensisa.lines.commands.DeleteCommand;
import ensisa.lines.commands.UndoRedoHistory;
import ensisa.lines.commands.UndoableCommand;
import ensisa.lines.model.Document;
import ensisa.lines.model.StraightLine;
import ensisa.lines.tools.Tool;
import ensisa.lines.tools.DrawTool;
import ensisa.lines.tools.SelectTool;
import ensisa.lines.tools.DeleteTool;
import javafx.collections.SetChangeListener;
import javafx.scene.control.MenuItem;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableSet;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.Pane;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;

public class MainController {

    private final Document document;
    private LinesEditor linesEditor;

    private final ObjectProperty<Tool> currentTool;
    private final UndoRedoHistory undoRedoHistory;
    private final HashMap<StraightLine, Object> lineControllers;
    private final SelectTool selectTool;
    private final DrawTool drawTool;
    private final ObservableSet<StraightLine> selectedLines;

    @FXML public Pane editorPane;
    @FXML private MenuItem undoMenuItem;
    @FXML private MenuItem redoMenuItem;
    @FXML private MenuItem deleteMenuItem;

    @FXML private TextField lineWidthTextField;
    @FXML private ColorPicker colorPicker;

    private StraightLine resizingLine = null;
    private boolean resizingStart = false;

    public MainController() {
        document = new Document();
        lineControllers = new HashMap<>();
        selectTool = new SelectTool(this);
        drawTool = new DrawTool(this);
        currentTool = new SimpleObjectProperty<>(selectTool);
        selectedLines = FXCollections.observableSet();
        undoRedoHistory = new UndoRedoHistory();
    }

    public Document getDocument() { return document; }
    public ObservableSet<StraightLine> getSelectedLines() { return selectedLines; }
    public void deselectAll() { selectedLines.clear(); }
    public Tool getCurrentTool() { return currentTool.get(); }

    public void initialize() {
        linesEditor = new LinesEditor(editorPane, this); // ✅ IMPORTANT
        initializeMenus();
        initializeInspector();
        observeDocument();
        observeSelection();
    }

    public void execute(UndoableCommand command) {
        undoRedoHistory.execute(command);
    }

    public void updateSelectionRect(StraightLine line) {
        linesEditor.updateSelectionRect(line);
    }

    private void initializeInspector() {
        selectedLines.addListener((SetChangeListener<StraightLine>) change -> {
            lineWidthTextField.setText(findCommonStrokeWidth());
            colorPicker.setValue(findCommonColor());
        });
    }

    private String findCommonStrokeWidth() {
        boolean foundOne = false;
        double width = 0.0;

        for (var l : selectedLines) {
            if (!foundOne) {
                width = l.getStrokeWidth();
                foundOne = true;
            } else if (width != l.getStrokeWidth()) return "";
        }
        return foundOne ? String.valueOf(width) : "";
    }

    private javafx.scene.paint.Color findCommonColor() {
        boolean foundOne = false;
        javafx.scene.paint.Color color = javafx.scene.paint.Color.TRANSPARENT;

        for (var l : selectedLines) {
            if (!foundOne) {
                color = l.getColor();
                foundOne = true;
            } else if (!color.equals(l.getColor())) return javafx.scene.paint.Color.TRANSPARENT;
        }
        return foundOne ? color : javafx.scene.paint.Color.TRANSPARENT;
    }

    @FXML
    private void lineWidthTextFieldAction() {
        try {
            var value = Double.parseDouble(lineWidthTextField.getText());
            if (value >= 1.0) selectedLines.forEach(line -> line.setStrokeWidth(value));
        } catch (NumberFormatException ignored) {}
    }

    @FXML
    private void colorPickerAction() {
        var color = colorPicker.getValue();
        selectedLines.forEach(line -> line.setColor(color));
    }

    private void observeSelection() {
        selectedLines.addListener((SetChangeListener<StraightLine>) change -> {
            if (change.wasAdded()) linesEditor.select(change.getElementAdded());
            if (change.wasRemoved()) linesEditor.deselect(change.getElementRemoved());
        });
    }

    private void initializeMenus() {
        undoMenuItem.disableProperty().bind(undoRedoHistory.canUndoProperty().not());
        redoMenuItem.disableProperty().bind(undoRedoHistory.canRedoProperty().not());
        deleteMenuItem.disableProperty().bind(
                javafx.beans.binding.Bindings.createBooleanBinding(
                        () -> selectedLines.isEmpty(), selectedLines
                )
        );
    }

    private void observeDocument() {
        document.getLines().addListener((ListChangeListener<StraightLine>) c -> {
            while (c.next()) {
                for (StraightLine line : c.getRemoved()) linesEditor.removeLine(line);
                for (StraightLine line : c.getAddedSubList()) linesEditor.createLine(line);
            }
        });
    }

    public void handlePressed(Rectangle handle, MouseEvent e) {
        if (!(currentTool.get() instanceof SelectTool)) return;

        for (var entry : linesEditor.startHandles.entrySet()) {
            if (entry.getValue() == handle) {
                resizingLine = entry.getKey();
                resizingStart = true;
                return;
            }
        }
        for (var entry : linesEditor.endHandles.entrySet()) {
            if (entry.getValue() == handle) {
                resizingLine = entry.getKey();
                resizingStart = false;
                return;
            }
        }
    }

    public void handleDragged(Rectangle handle, MouseEvent e) {
        if (resizingLine == null) return;

        if (resizingStart) {
            resizingLine.setStartX(e.getX());
            resizingLine.setStartY(e.getY());
        } else {
            resizingLine.setEndX(e.getX());
            resizingLine.setEndY(e.getY());
        }

        linesEditor.updateSelectionRect(resizingLine);
    }

    public void handleReleased() {
        resizingLine = null;
    }

    @FXML private void undoMenuItemAction() { undoRedoHistory.undo(); }
    @FXML private void redoMenuItemAction() { undoRedoHistory.redo(); }
    @FXML private void deleteMenuItemAction() { undoRedoHistory.execute(new DeleteCommand(this)); }

    @FXML private void selectDrawTool() { currentTool.set(new DrawTool(this)); }
    @FXML private void selectSelectTool() { currentTool.set(new SelectTool(this)); }
    @FXML private void selectDeleteTool() { currentTool.set(new DeleteTool(this)); }

    @FXML private void mousePressedInEditor(MouseEvent e) { getCurrentTool().mousePressed(e); }
    @FXML private void mouseDraggedInEditor(MouseEvent e) { getCurrentTool().mouseDragged(e); }
    @FXML private void mouseReleasedInEditor(MouseEvent e) { getCurrentTool().mouseReleased(e); }

    @FXML private void quitMenuAction() { javafx.application.Platform.exit(); }
}
