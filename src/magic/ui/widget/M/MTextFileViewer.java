package magic.ui.widget.M;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Path;
import java.util.function.Function;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import magic.ui.FontsAndBorders;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.TexturedPanel;
import magic.utility.FileIO;
import net.miginfocom.swing.MigLayout;

/**
 * Displays contents of given text file.
 * Includes optional banner with clickable link which opens the file in the
 * default text editor or opens the containing directory in file explorer.
 */
@SuppressWarnings("serial")
public class MTextFileViewer extends MWidget {

    private static final Font FILE_LINK_FONT = new Font("dialog", Font.PLAIN, 14);
    private static final Color FILE_LINK_COLOR = Color.WHITE;

    private final TexturedPanel panel = new TexturedPanel();
    private final MPlainTextViewer textViewer = new MPlainTextViewer();
    private final MFileLink fileLink = new MFileLink();

    public MTextFileViewer() {
        setDefaultProperties();
        setLayout();
    }

    protected void setLayout() {
        panel.removeAll();
        panel.add(fileLink.component(), "w 100%, h 34!, hidemode 3");
        panel.add(textViewer.component(), "w 100%, h 100%");
        panel.revalidate();
    }

    private void setDefaultProperties() {

        panel.setLayout(new MigLayout("insets 0, gap 0, flowy"));
        panel.setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);

        fileLink.setOpaque(true);
        fileLink.setBackground(Color.DARK_GRAY);
        fileLink.setForeground(Color.WHITE);
        fileLink.setFont(FILE_LINK_FONT);
        fileLink.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));
        fileLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                fileLink.setForeground(MagicStyle.getRolloverColor());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                fileLink.setForeground(FILE_LINK_COLOR);
            }
        });
    }

    protected String getFileContents(final Path textFilePath) {
        try {
            return FileIO.toStr(new File(textFilePath.toString()));
        } catch (final java.io.IOException ex) {
            return textFilePath.toAbsolutePath().toString() + "\n" + ex.toString();
        }
    }

    public void setTextFile(final File aFile) {
        textViewer.setText(getFileContents(aFile.toPath()));
        fileLink.setFile(aFile.toPath());
    }

    public void setTextFile(final Path textFilePath) {
        textViewer.setText(getFileContents(textFilePath));
        fileLink.setFile(textFilePath);
    }

    /**
     * Before displaying the text from the file this applies a function
     * that can be used to alter what is actually shown.
     */
    public void setTextFile(Path textFilePath, Function<String, String> filter) {
        textViewer.setText(filter.apply(getFileContents(textFilePath)));
        fileLink.setFile(textFilePath);
    }

    public void setFileLinkVisible(boolean b) {
        fileLink.setVisible(b);
        setLayout();
    }

    @Override
    public JComponent component() {
        return panel;
    }
}
