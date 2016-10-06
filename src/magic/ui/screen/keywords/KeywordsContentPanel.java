package magic.ui.screen.keywords;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import magic.data.KeywordDefinitions;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class KeywordsContentPanel extends TexturedPanel {
    
    private ScrollablePanel scrollablePanel;
    private JScrollPane scrollPane;

    KeywordsContentPanel() {
        setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);
        setLayout(new MigLayout("insets 0, gap 0"));
        createScrollablePanel();
        addNewScrollPane();
        createKeywordsPanel();
    }

    private void setScrollbarToTop() {
        scrollPane.revalidate();
        SwingUtilities.invokeLater(() -> {
            scrollPane.getVerticalScrollBar().setValue(0);
        });
    }

    private void createKeywordsPanel() {

        final Color nameColor = MagicStyle.getTheme().getColor(Theme.COLOR_NAME_FOREGROUND);
        final Color textColor = MagicStyle.getTheme().getTextColor();

        scrollablePanel.removeAll();

        for (final KeywordDefinitions.KeywordDefinition keywordDefinition : KeywordDefinitions.getKeywordDefinitions()) {

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


    /**
     * Creates a transparent scroll pane that handles the scrolling
     * characteristics for the list of {@code JTextArea} entries.
     */
    private void addNewScrollPane() {
        scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(50);
        scrollPane.getVerticalScrollBar().setBlockIncrement(50);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getViewport().add(scrollablePanel);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, "w 100%, h 100%");
    }

    /**
     * Creates a {@code Scrollable JPanel} that works better with
     * {@code JScrollPane} than the standard {@code JPanel}.
     * <p>
     * This manages the layout and display of the list of {@code JTextArea} entries.
     */
    private void createScrollablePanel() {
        scrollablePanel = new ScrollablePanel();
        scrollablePanel.setLayout(new MigLayout("insets 10, gap 6 8, wrap 2"));
        scrollablePanel.setOpaque(false);
    }
    
}
