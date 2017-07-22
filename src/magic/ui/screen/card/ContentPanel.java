package magic.ui.screen.card;

import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.model.MagicCardDefinition;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ContentPanel extends JPanel
    implements ITabsProvider {

    // UI components
    private final CardSideBar sidebar;
    private final TabsContainer tabsPanel;
    private CardScriptPanel scriptPanel;

    private final MigLayout migLayout = new MigLayout();
    private final MagicCardDefinition card;

    ContentPanel(final MagicCardDefinition card) {

        this.card = card;

        sidebar = new CardSideBar();
        sidebar.setCard(card);

        tabsPanel = new TabsContainer(this);
        tabsPanel.addTab("Script");
        tabsPanel.setTab("Script");

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
        add(tabsPanel, "w 100%, h 100%");
    }

    private void setLookAndFeel() {
        setLayout(migLayout);
        setOpaque(false);
    }

    private CardScriptPanel getScriptPanel() {
        if (scriptPanel == null) {
            scriptPanel = new CardScriptPanel(card);
        }
        return scriptPanel;
    }

    private JPanel getUnimplementedPanel(String tabName) {
        JPanel p = new JPanel();
        JLabel lbl = new JLabel("Not implemented yet : " + tabName);
        p.add(lbl);
        return p;
    }

    @Override
    public JPanel getTabPanel(String tabName) {
        switch (tabName) {
        case "Script":
            return getScriptPanel();
        default:
            return getUnimplementedPanel(tabName);
        }
    }
}
