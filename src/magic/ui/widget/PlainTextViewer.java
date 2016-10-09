package magic.ui.widget;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import magic.ui.FontsAndBorders;

/**
 * Displays read-only text using mono-spaced font with built-in scrolling.
 */
@SuppressWarnings("serial")
public class PlainTextViewer extends JScrollPane {
    
    private final JTextArea textArea;

    public PlainTextViewer() {
        textArea = new JTextArea();
        setDefaultProperties();
        setViewportView(textArea);
    }

    private void setDefaultProperties() {

        textArea.setBackground(FontsAndBorders.TEXTAREA_TRANSPARENT_COLOR_HACK);
        textArea.setEditable(false);
        textArea.setFont(FontsAndBorders.FONT_README);
        textArea.setFocusable(false);

        setBorder(FontsAndBorders.BLACK_BORDER);
        setOpaque(false);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        getVerticalScrollBar().setUnitIncrement(50);
        getVerticalScrollBar().setBlockIncrement(50);
        getViewport().setOpaque(false);
    }

    private void resetVerticalScrollbar() {
        SwingUtilities.invokeLater(() -> {
            getVerticalScrollBar().setValue(0);
        });
    }    

    public void setText(String text) {
        textArea.setText(text);
        resetVerticalScrollbar();
    }
}
