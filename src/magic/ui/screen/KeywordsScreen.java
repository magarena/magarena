package magic.ui.screen;

import magic.data.KeywordDefinitions;
import magic.data.KeywordDefinitions.KeywordDefinition;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.widget.MenuButton;
import magic.ui.theme.Theme;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.List;
import magic.ui.utility.MagicStyle;

@SuppressWarnings("serial")
public class KeywordsScreen extends AbstractScreen implements IStatusBar, IActionBar {

    private static JScrollPane scrollPane;
    private static MyScrollablePanel scrollablePanel;

    public KeywordsScreen() {
        final JPanel content = new TexturedPanel();
        content.setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);
        content.setLayout(new MigLayout("insets 0, gap 0"));
        createScrollablePanel();
        addNewScrollPane(content);
        createKeywordsPanel();
        setContent(content);
    }

    /**
     * Creates a transparent scroll pane that handles the scrolling
     * characteristics for the list of {@code JTextArea} entries.
     */
    private static void addNewScrollPane(final JPanel content) {
        scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(50);
        scrollPane.getVerticalScrollBar().setBlockIncrement(50);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getViewport().add(scrollablePanel);
        scrollPane.getViewport().setOpaque(false);
        content.add(scrollPane, "w 100%, h 100%");
    }

    /**
     * Creates a {@code Scrollable JPanel} that works better with
     * {@code JScrollPane} than the standard {@code JPanel}.
     * <p>
     * This manages the layout and display of the list of {@code JTextArea} entries.
     */
    private static void createScrollablePanel() {
        scrollablePanel = new MyScrollablePanel();
        scrollablePanel.setLayout(new MigLayout("insets 10, gap 6 8, wrap 2"));
        scrollablePanel.setOpaque(false);
    }

    private static void setScrollbarToTop() {
        scrollPane.revalidate();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final JScrollBar scrollbar = scrollPane.getVerticalScrollBar();
                scrollbar.setValue(0);
            }
        });
    }

    private static void createKeywordsPanel() {

        final Color nameColor = MagicStyle.getTheme().getColor(Theme.COLOR_NAME_FOREGROUND);
        final Color textColor = MagicStyle.getTheme().getTextColor();

        scrollablePanel.removeAll();

        for (final KeywordDefinition keywordDefinition : KeywordDefinitions.getInstance().getKeywordDefinitions()) {

            final JPanel keywordPanel = new JPanel(new MigLayout("insets 0, gap 0, flowy"));
            keywordPanel.setOpaque(false);

            final JLabel nameLabel = new JLabel(keywordDefinition.name);
            nameLabel.setForeground(nameColor);
            nameLabel.setFont(FontsAndBorders.FONT2);
            keywordPanel.add(nameLabel, "w 100%");

            final JTextArea descriptionLabel = new JTextArea(keywordDefinition.description.replace("<br>", " "));
            descriptionLabel.setBackground(FontsAndBorders.TEXTAREA_TRANSPARENT_COLOR_HACK);
            descriptionLabel.setBorder(null);
            descriptionLabel.setLineWrap(true);
            descriptionLabel.setWrapStyleWord(true);
            descriptionLabel.setForeground(textColor);
            keywordPanel.add(descriptionLabel, "w 10:100%");

            scrollablePanel.add(keywordPanel, "w 10:100%, top");
        }

        setScrollbarToTop();

    }

    protected static final class MyScrollablePanel extends JPanel implements Scrollable {

        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        public int getScrollableUnitIncrement(final Rectangle visibleRect, final int orientation, final int direction) {
            return getFont().getSize();
        }

        public int getScrollableBlockIncrement(final Rectangle visibleRect, final int orientation, final int direction) {
            return getFont().getSize();
        }

        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        // we don't want to track the height, because we want to scroll vertically.
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see magic.ui.IMagScreenStatusBar#getScreenCaption()
     */
    @Override
    public String getScreenCaption() {
        return "Keywords Glossary";
    }

    /*
     * (non-Javadoc)
     *
     * @see magic.ui.IMagActionBar#getLeftAction()
     */
    @Override
    public MenuButton getLeftAction() {
        return MenuButton.getCloseScreenButton("Close");
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
