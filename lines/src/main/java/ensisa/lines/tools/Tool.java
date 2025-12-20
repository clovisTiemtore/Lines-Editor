package ensisa.lines.tools;

import javafx.scene.input.MouseEvent;

public interface Tool {
    default void mousePressed(MouseEvent event) {}
    default void mouseDragged(MouseEvent event) {}
    default void mouseReleased(MouseEvent event) {}
    default void mouseEntered(MouseEvent event) {}
    default void mouseExited(MouseEvent event) {}
}