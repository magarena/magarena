package magic.ui.widget.M;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import magic.ui.FontsAndBorders;

/**
 * Displays read-only text using mono-spaced font with built-in scrolling.
 */
@SuppressWarnings("serial")
public class MPlainTextViewer extends MWidget {

    private final JScrollPane scrollPane = new JScrollPane();
    private final JTextArea textArea = new JTextArea();

    public MPlainTextViewer() {
        setDefaultProperties();       
    }

    private void setDefaultProperties() {

        textArea.setBackground(FontsAndBorders.TEXTAREA_TRANSPARENT_COLOR_HACK);
        textArea.setEditable(false);
        textArea.setFont(FontsAndBorders.FONT_README);
        textArea.setFocusable(false);

        scrollPane.setBorder(FontsAndBorders.BLACK_BORDER);
        scrollPane.setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(50);
        scrollPane.getVerticalScrollBar().setBlockIncrement(50);
        scrollPane.getViewport().setOpaque(false);

        scrollPane.setViewportView(textArea);
    }

    private void resetVerticalScrollbar() {
        SwingUtilities.invokeLater(() -> {
            scrollPane.getVerticalScrollBar().setValue(0);
        });
    }    

    public void setText(String text) {
        textArea.setText(text);
        resetVerticalScrollbar();
    }

    @Override
    public JComponent component() {
        return scrollPane;
    }
}
