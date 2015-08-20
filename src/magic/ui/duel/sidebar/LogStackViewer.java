package magic.ui.duel.sidebar;

import java.awt.Component;
import java.awt.Rectangle;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class LogStackViewer extends TexturedPanel {

    private final LogViewer logBookViewer;
    private final StackViewer stackViewer;

    LogStackViewer(LogViewer logBookViewer0, StackViewer stackViewer0) {
        logBookViewer = logBookViewer0;
        stackViewer = stackViewer0;
        
        setBorder(FontsAndBorders.BLACK_BORDER);
        setOpaque(false);
        
        setLayout(new MigLayout("insets 0, gap 0, flowy", "", "[shrinkprio 200][]"));
        add(logBookViewer, "w 100%, h 80:100%");
        add(stackViewer, "w 100%");
    }

    Rectangle getStackViewerRectangle(Component canvas) {
        return stackViewer.getStackViewerRectangle(canvas);
    }
}
