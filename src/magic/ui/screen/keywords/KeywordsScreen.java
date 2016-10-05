package magic.ui.screen.keywords;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import magic.data.KeywordDefinitions.KeywordDefinition;
import magic.data.KeywordDefinitions;
import magic.translate.UiString;
import magic.ui.screen.AbstractScreen;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.widget.MenuButton;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class KeywordsScreen extends AbstractScreen implements IStatusBar, IActionBar {

    // translatable strings
    private static final String _S1 = "Keywords Glossary";
    private static final String _S2 = "Close";

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

        for (final KeywordDefinition keywordDefinition : KeywordDefinitions.getKeywordDefinitions()) {

            final JPanel keywordPanel = new JPanel(new MigLayout("insets 0, gap 0, flowy"));
            keywordPanel.setOpaque(false);

            final JLabel nameLabel = new JLabel(keywordDefinition.name);
            nameLabel.setForeground(nameColor);
            nameLabel.setFont(FontsAndBorders.FONT2);
            keywordPanel.add(nameLabel, "w 100%");

            final JLabel descriptionLabel = new JLabel();
            descriptionLabel.setText("<html>" + keywordDefinition.description.replace("<br>", " ") + "</html>");
            descriptionLabel.setForeground(textColor);
            keywordPanel.add(descriptionLabel, "w 10:100%");

            scrollablePanel.add(keywordPanel, "w 10:100%, top");
        }

        setScrollbarToTop();

    }

    protected static final class MyScrollablePanel extends JPanel implements Scrollable {

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(final Rectangle visibleRect, final int orientation, final int direction) {
            return getFont().getSize();
        }

        @Override
        public int getScrollableBlockIncrement(final Rectangle visibleRect, final int orientation, final int direction) {
            return getFont().getSize();
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        // we don't want to track the height, because we want to scroll vertically.
        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

    @Override
    public String getScreenCaption() {
        return UiString.get(_S1);
    }

    @Override
    public MenuButton getLeftAction() {
        return MenuButton.getCloseScreenButton(UiString.get(_S2));
    }

    @Override
    public MenuButton getRightAction() {
        return null;
    }

    @Override
    public List<MenuButton> getMiddleActions() {
        return null;
    }

    @Override
    public JPanel getStatusPanel() {
        return null;
    }

}
