package magic.ui.widget;

import magic.ui.FontsAndBorders;
import java.io.File;
import java.nio.file.Path;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import magic.utility.FileIO;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class TextFileReaderPanel extends TexturedPanel {

    private final MigLayout migLayout = new MigLayout();
    private final JTextArea textArea = new JTextArea();
    private final JScrollPane scrollPane = new JScrollPane(textArea);

    public TextFileReaderPanel() {
        setLookAndFeel();
        refreshLayout();
    }

    private void setLookAndFeel() {
        setLayout(migLayout);
        setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);
        // file contents text area
        textArea.setBackground(FontsAndBorders.TEXTAREA_TRANSPARENT_COLOR_HACK);
        textArea.setEditable(false);
        textArea.setFont(FontsAndBorders.FONT_README);
        // scroll pane
        scrollPane.setBorder(FontsAndBorders.BLACK_BORDER);
        scrollPane.setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(50);
        scrollPane.getVerticalScrollBar().setBlockIncrement(50);
        scrollPane.getViewport().setOpaque(false);
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("insets 0, gap 0, center");
        add(scrollPane, "w 100%, h 100%");
    }

    public void setContent(final Path textFilePath) {
        textArea.setText(getFileContents(textFilePath));
        refreshLayout();
        resetVerticalScrollbar();
    }

    private String getFileContents(final Path textFilePath) {
        try {
            final String fileContents = FileIO.toStr(new File(textFilePath.toString()));
            return fileContents;
        } catch (final java.io.IOException ex) {
            return textFilePath.toAbsolutePath().toString() + "\n" + ex.toString();
        }
    }

    private void resetVerticalScrollbar() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                scrollPane.getVerticalScrollBar().setValue(0);
            }
        });
    }

}
