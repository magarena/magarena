package magic.ui.screen.card;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class TabsContainer extends JPanel {

    // UI components
    private final TabsPanel tabsPanel;
    private JPanel activePanel;

    private final MigLayout migLayout = new MigLayout();
    private final ITabsProvider tabsProvider;

    public TabsContainer(final ITabsProvider provider) {
        this.tabsProvider = provider;
        tabsPanel = new TabsPanel();
        activePanel = new JPanel();
        setLayout(migLayout);
        setLayout();
        setOpaque(false);
    }

    private void setLayout() {
        tabsPanel.setVisible(tabsPanel.getTabsCount() > 1);
        removeAll();
        migLayout.setLayoutConstraints("flowy, insets 0, gap 0");
        add(tabsPanel, "w 100%, h 30!, hidemode 3");
        add(activePanel, "w 100%, h 100%");
        revalidate();
        repaint();
    }

    void setTab(String tabName) {
        tabsPanel.setSelectedToggleButton(tabName);
        activePanel = tabsProvider.getTabPanel(tabName);
        setLayout();
    }

    void addTab(String name) {
        tabsPanel.addToggleButton(name, new AbstractAction(name) {
            @Override
            public void actionPerformed(ActionEvent e) {
                setTab(e.getActionCommand());
            }
        });
        tabsPanel.refreshLayout();
    }

}
