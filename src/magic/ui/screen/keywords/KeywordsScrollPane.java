package magic.ui.screen.keywords;

import java.io.IOException;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

/**
 * A transparent scroll pane that handles the scrolling
 * characteristics for the list of {@code JTextArea} entries.
*/
@SuppressWarnings("serial")
class KeywordsScrollPane extends JScrollPane {

    KeywordsScrollPane() throws IOException {
        setDefaultProperties();
        getViewport().add(new KeywordsScrollablePanel());
        setScrollbarToTop();
    }

    private void setDefaultProperties() {
        setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        getVerticalScrollBar().setUnitIncrement(50);
        getVerticalScrollBar().setBlockIncrement(50);
        setOpaque(false);
        setBorder(null);
        getViewport().setOpaque(false);
    }

    private void setScrollbarToTop() {
        SwingUtilities.invokeLater(() -> getVerticalScrollBar().setValue(0));
    }
}
