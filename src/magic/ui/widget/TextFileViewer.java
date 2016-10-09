package magic.ui.widget;

import magic.ui.widget.M.MPlainTextViewer;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Path;
import javax.swing.BorderFactory;
import magic.ui.FontsAndBorders;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.M.MFileLink;
import magic.utility.FileIO;
import net.miginfocom.swing.MigLayout;

/**
 * Displays contents of given text file.
 * Includes banner with clickable link which opens the file in the default
 * text editor or opens the containing directory in file explorer.
 */
@SuppressWarnings("serial")
public class TextFileViewer extends TexturedPanel {

    private static final Font FILE_LINK_FONT = new Font("dialog", Font.PLAIN, 14);
    private final Color FILE_LINK_COLOR = Color.WHITE;

    private final MPlainTextViewer textViewer = new MPlainTextViewer();
    private final MFileLink fileLink = new MFileLink();

    public TextFileViewer() {
        setDefaultProperties();
        setLayout();
    }

    protected void setLayout() {
        removeAll();
        add(fileLink.component(), "w 100%, h 34!, hidemode 3");
        add(textViewer.component(), "w 100%, h 100%");
        revalidate();
    }

    private void setDefaultProperties() {

        setLayout(new MigLayout("insets 0, gap 0, flowy"));
        setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);

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

    public void setTextFile(final Path textFilePath) {
        textViewer.setText(getFileContents(textFilePath));
        fileLink.setFile(textFilePath);
    }

    protected void setFileLinkVisible(boolean b) {
        fileLink.setVisible(b);
        setLayout();
    }
}
