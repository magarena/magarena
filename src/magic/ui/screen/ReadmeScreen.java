package magic.ui.screen;

import magic.data.FileIO;
import magic.ui.screen.interfaces.IMagActionBar;
import magic.ui.screen.interfaces.IMagStatusBar;
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
import java.util.List;

@SuppressWarnings("serial")
public class ReadmeScreen extends AbstractScreen implements IMagStatusBar, IMagActionBar {

    private static final String README_FILENAME="README.txt";

    public ReadmeScreen() {
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
        try { //load content from README.txt file
            fileContents = FileIO.toStr(new java.io.File(README_FILENAME));
        } catch (final java.io.IOException ex) {
            fileContents = README_FILENAME + " not found at " + System.getProperty("user.dir");
            System.err.println("WARNING! " + fileContents);
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
        return"README.txt";
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

}
