package magic.ui.duel.viewer;

import java.awt.Component;
import java.awt.Rectangle;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class LogStackViewer extends TexturedPanel implements IStackViewerListener  {

    private final LogBookViewer logBookViewer;
    private final StackViewer stackViewer;

    public LogStackViewer(LogBookViewer logBookViewer0, StackViewer stackViewer0) {
        logBookViewer = logBookViewer0;
        logBookViewer.setOpaque(false);
        stackViewer = stackViewer0;
        stackViewer.addListener(this);
        setBorder(FontsAndBorders.BLACK_BORDER);
    }

    @Override
    public void stackViewerUpdated() {
        setLogStackLayout();
        revalidate();
    }

    /**
     * The log should expand or contract depending on the height of the stack.
     * If the stack would cause the log to shrink below 60 pixels then stack
     * will cease expanding and use a vertical scrollbar instead.
     */
    public void setLogStackLayout() {

        int viewerHeight = getSize().height;
        int stackHeight = stackViewer.getPreferredSize().height;
        int logHeight = 60; // minimum log height.

        if (stackHeight > 0) {
            if (viewerHeight - stackHeight < logHeight) {
                stackHeight = viewerHeight - logHeight;
            } else {
                logHeight = viewerHeight - stackHeight;
            }
            String rowConstraints = "[" + logHeight + "][" + stackHeight + "]";

            removeAll();
            setLayout(new MigLayout("insets 0, gap 0, flowy", "", rowConstraints));

            add(logBookViewer, "w 100%, h 100%");
            add(stackViewer, "w 100%, h 100%");
        }

    }

    public Rectangle getStackViewerRectangle(Component canvas) {
        return stackViewer.getStackViewerRectangle(canvas);
    }

}
