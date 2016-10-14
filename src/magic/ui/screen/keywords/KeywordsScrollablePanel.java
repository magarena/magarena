package magic.ui.screen.keywords;

import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import net.miginfocom.swing.MigLayout;

/**
 * A {@code Scrollable JPanel} that works better with
 * {@code JScrollPane} than the standard {@code JPanel}.
 * <p>
 * This manages the layout and display of the list of {@code JTextArea} entries.
 */
@SuppressWarnings("serial")
class KeywordsScrollablePanel extends JPanel implements Scrollable {

    KeywordsScrollablePanel() {
        setLayout(new MigLayout("insets 10, gap 6 8, wrap 2"));
        setOpaque(false);
        refreshKeywords();
    }

    private void refreshKeywords() {
        for (final Keyword keywordDefinition : KeywordsHelper.loadKeywordsFileToSortedArray())  {
            add(new KeywordPanelA(keywordDefinition), "w 10:100%, top");
        }
    }

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
