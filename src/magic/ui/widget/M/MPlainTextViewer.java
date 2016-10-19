package magic.ui.widget.M;

import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import magic.ui.FontsAndBorders;

/**
 * Displays read-only text using mono-spaced font with built-in scrolling.
 */
@SuppressWarnings("serial")
public class MPlainTextViewer extends MWidget {

    private final MScrollPane scrollPane = new MScrollPane();
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
        scrollPane.setHScrollBarAsNeeded();
        scrollPane.setVScrollBarIncrement(50);
        scrollPane.setVScrollBarBlockIncrement(50);

        scrollPane.setViewportView(textArea);
    }

    private void resetVerticalScrollbar() {
        SwingUtilities.invokeLater(() -> {
            scrollPane.setVScrollBarValue(0);
        });
    }    

    public void setText(String text) {
        textArea.setText(text);
        resetVerticalScrollbar();
    }

    @Override
    public JComponent component() {
        return scrollPane.component();
    }
}
