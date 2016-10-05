package magic.ui.screen.card.explorer;

import magic.model.MagicCardDefinition;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ExplorerSidebarPanel extends TexturedPanel {

    private final MigLayout migLayout = new MigLayout();
    private final CardPanel cardPanel = new CardPanel();

    public ExplorerSidebarPanel() {
        setLookAndFeel();
        refreshLayout();
    }

    private void setLookAndFeel() {
        setLayout(migLayout);
        setOpaque(true);
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("flowy, insets 0");
        add(cardPanel, "h 100%");
        revalidate();
    }

    public void setCard(final MagicCardDefinition card) {
        cardPanel.setCard(card);
    }

}
