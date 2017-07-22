package magic.ui.screen.card;

import javax.swing.JPanel;
import magic.model.MagicCardDefinition;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ContentPanel extends JPanel {

    // UI components
    private final MigLayout migLayout = new MigLayout();
    private final CardSideBar sidebar;
    private final CardScriptPanel scriptPanel;

    ContentPanel(final MagicCardDefinition card) {

        sidebar = new CardSideBar();
        sidebar.setCard(card);

        scriptPanel = new CardScriptPanel(card);

        setLookAndFeel();
        setLayout();
        refreshContent();
    }

    void refreshContent() {
        scriptPanel.refreshContent();
    }

    private void setLayout() {
        removeAll();
        migLayout.setLayoutConstraints("insets 0, gap 0");
        add(sidebar, "h 100%");
        add(scriptPanel, "w 100%, h 100%");
    }

    private void setLookAndFeel() {
        setLayout(migLayout);
        setOpaque(false);
    }
}
