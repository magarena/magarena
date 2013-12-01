package magic.ui;

import magic.data.FileIO;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.MenuButton;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.List;

@SuppressWarnings("serial")
public class ReadmeScreen extends MagScreen implements IMagStatusBar, IMagActionBar {

    private static final String README_FILENAME="README.txt";

    private final MagicFrame frame;
    private static JScrollPane keywordsPane;

    public ReadmeScreen(final MagicFrame frame0) {
        super(getScreenContent(frame0), frame0);
        this.frame = frame0;
    }

    private static JPanel getScreenContent(final MagicFrame frame0) {

        final JPanel content = new TexturedPanel();

        content.setBackground(new Color(255, 255, 255, 60));
        content.setLayout(new MigLayout("insets 0, gap 0, center"));

        final JTextArea readMeTextArea = new JTextArea();
        readMeTextArea.setBackground(new Color(255, 255, 255, 220));
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

        keywordsPane = new JScrollPane(readMeTextArea);
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
            public void actionPerformed(ActionEvent e) {
                frame.closeActiveScreen(false);
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
    public boolean isScreenReadyToClose(MagScreen nextScreen) {
        return true;
    }

}
