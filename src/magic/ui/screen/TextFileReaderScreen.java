package magic.ui.screen;

import magic.data.FileIO;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.widget.MenuButton;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Path;
import java.util.List;

@SuppressWarnings("serial")
public class TextFileReaderScreen extends AbstractScreen implements IStatusBar, IActionBar {

    private final Path textFilePath;

    public TextFileReaderScreen(final Path textFilePath) {
        this.textFilePath = textFilePath;
        setContent(getScreenContent());
    }

    private JPanel getScreenContent() {

        final JPanel content = new TexturedPanel();

        content.setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);
        content.setLayout(new MigLayout("insets 0, gap 0, center"));

        final JTextArea readMeTextArea = new JTextArea();
        readMeTextArea.setBackground(FontsAndBorders.TEXTAREA_TRANSPARENT_COLOR_HACK);
        readMeTextArea.setEditable(false);
        readMeTextArea.setFont(FontsAndBorders.FONT_README);

        String fileContents = "";
        try {
            fileContents = FileIO.toStr(new File(textFilePath.toString()));
        } catch (final java.io.IOException ex) {
            System.err.println("Invalid file path : " + textFilePath.toAbsolutePath().toString());
        }
        readMeTextArea.setText(fileContents);

        final JScrollPane keywordsPane = new JScrollPane(readMeTextArea);
        keywordsPane.setBorder(FontsAndBorders.BLACK_BORDER);
        keywordsPane.setOpaque(false);
        keywordsPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        keywordsPane.getVerticalScrollBar().setUnitIncrement(50);
        keywordsPane.getVerticalScrollBar().setBlockIncrement(50);
        keywordsPane.getViewport().setOpaque(false);
        content.add(keywordsPane, "w 100%, h 100%");

        SwingUtilities.invokeLater(new Runnable() {
            public void run()
            {
                keywordsPane.getVerticalScrollBar().setValue(0);
            }
        });

        return content;
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getLeftAction()
     */
    @Override
    public MenuButton getLeftAction() {
        return new MenuButton("Close", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                getFrame().closeActiveScreen(false);
            }
        });
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagScreenStatusBar#getScreenCaption()
     */
    @Override
    public String getScreenCaption() {
        return textFilePath.getFileName().toString();
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getRightAction()
     */
    @Override
    public MenuButton getRightAction() {
        return null;
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getMiddleActions()
     */
    @Override
    public List<MenuButton> getMiddleActions() {
        return null;
    }

    /* (non-Javadoc)
     * @see magic.ui.MagScreen#isScreenReadyToClose(magic.ui.MagScreen)
     */
    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        return true;
    }

    /* (non-Javadoc)
     * @see magic.ui.screen.interfaces.IStatusBar#getStatusPanel()
     */
    @Override
    public JPanel getStatusPanel() {
        return null;
    }

}
