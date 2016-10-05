package magic.ui.screen.deck.editor;

import java.awt.Color;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class DeckActionPanel extends TexturedPanel {

    public static final Color BACKGROUND_COLOR = MagicStyle.getTranslucentColor(Color.DARK_GRAY, 230);

    private static final JPanel EMPTY_PANEL = new JPanel();
    static {
        EMPTY_PANEL.setOpaque(false);
    }

    private final MigLayout migLayout = new MigLayout();
    private final CardQuantityActionPanel quantityActionPanel;
    private final JPanel viewActionPanel;

    DeckActionPanel() {
        this(null, null);
    }

    DeckActionPanel(final AbstractAction plusButtonAction, final AbstractAction minusButtonAction) {

        setBackground(BACKGROUND_COLOR);

        viewActionPanel = new JPanel(new MigLayout("insets 4 0 0 0, flowy"));
        viewActionPanel.setOpaque(false);

        quantityActionPanel = new CardQuantityActionPanel(plusButtonAction, minusButtonAction);

        setLayout(migLayout);
        refreshLayout();
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("insets 0, flowy");
        add(viewActionPanel, "w 100%, h 100%");
        add(quantityActionPanel, "w 100%");
        add(EMPTY_PANEL, "w 100%, h 100%");
        revalidate();
    }

    CardQuantityActionPanel getQuantityPanel() {
        return quantityActionPanel;
    }

    void setView(IDeckEditorView aView) {
        updateViewActionPanel(aView.getActionButtons());
        refreshLayout();
    }

    private void updateViewActionPanel(final List<ActionBarButton> actionButtons) {
        viewActionPanel.removeAll();
        for (ActionBarButton actionButton : actionButtons) {
            viewActionPanel.add(actionButton, "w " + MainViewsPanel.DECK_ACTION_PANEL_WIDTH + "!");
        }
        viewActionPanel.revalidate();
        viewActionPanel.repaint();
    }

}
