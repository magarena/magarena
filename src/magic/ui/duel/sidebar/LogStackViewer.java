package magic.ui.duel.sidebar;

import java.awt.Component;
import java.awt.Rectangle;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class LogStackViewer extends TexturedPanel {

    private final LogBookViewer logBookViewer;
    private final StackViewer stackViewer;

    LogStackViewer(LogBookViewer logBookViewer0, StackViewer stackViewer0) {
        logBookViewer = logBookViewer0;
        logBookViewer.setOpaque(false);
        stackViewer = stackViewer0;
        
        setBorder(FontsAndBorders.BLACK_BORDER);
        setLayout(new MigLayout("insets 0, gap 0, flowy", "", "[shrinkprio 200][]"));
        add(logBookViewer, "w 100%, h 60:100%");
        add(stackViewer, "w 100%");
    }

    Rectangle getStackViewerRectangle(Component canvas) {
        return stackViewer.getStackViewerRectangle(canvas);
    }
}
