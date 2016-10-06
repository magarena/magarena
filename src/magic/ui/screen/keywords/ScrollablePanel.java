package magic.ui.screen.keywords;

import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.Scrollable;

@SuppressWarnings("serial")
class ScrollablePanel extends JPanel implements Scrollable {

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(final Rectangle visibleRect, final int orientation, final int direction) {
        return getFont().getSize();
    }

    @Override
    public int getScrollableBlockIncrement(final Rectangle visibleRect, final int orientation, final int direction) {
        return getFont().getSize();
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    // we don't want to track the height, because we want to scroll vertically.
    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
