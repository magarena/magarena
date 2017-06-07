package magic.ui.screen.deck.editor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import magic.data.MagicIcon;
import magic.translate.MText;
import magic.ui.FontsAndBorders;
import magic.ui.MagicImages;
import magic.ui.screen.widget.ActionBarButton;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class CardQuantityActionPanel extends JPanel {

    // translatable strings
    private static final String _S1 = "Add card";
    private static final String _S2 = "Add selected card to deck (or left click row).";
    private static final String _S3 = "Remove card";
    private static final String _S4 = "Remove selected card from deck (or right click row).";
    private static final String _S5 = "Increase by one";
    private static final String _S6 = "Maximum is ten.";
    private static final String _S7 = "Decrease by one";
    private static final String _S9 = "Must use this button to remove last instance of a card.";

    // UI components
    private final ActionBarButton addButton;
    private final ActionBarButton minusButton;
    private final ActionBarButton incrementButton;
    private final ActionBarButton decrementButton;
    private final JLabel quantityLabel;
    //
    private final MigLayout migLayout = new MigLayout();
    private int max_quantity = 10;
    private int quantity = 1;

    CardQuantityActionPanel(final AbstractAction plusButtonAction, final AbstractAction minusButtonAction) {

        setOpaque(false);

        addButton = new ActionBarButton(
                MagicImages.getIcon(MagicIcon.PLUS_ICON),
                MText.get(_S1),
                MText.get(_S2),
                plusButtonAction);

        minusButton = new ActionBarButton(
                MagicImages.getIcon(MagicIcon.MINUS_ICON),
                MText.get(_S3),
                MText.get(String.format("%s<br>%s", MText.get(_S4), MText.get(_S9))),
                minusButtonAction);

        incrementButton = new ActionBarButton(
                MagicImages.getIcon(MagicIcon.ARROWUP),
                MText.get(_S5),
                MText.get(_S6),
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        quantity++;
                        if (quantity > max_quantity) {
                            quantity = max_quantity;
                        }
                        quantityLabel.setText(Integer.toString(quantity));
                    }
                });
        decrementButton = new ActionBarButton(
                MagicImages.getIcon(MagicIcon.ARROWDOWN),
                MText.get(_S7),
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        quantity--;
                        if (quantity < 1) {
                            quantity = 1;
                        }
                        quantityLabel.setText(Integer.toString(quantity));
                    }
                });

        quantityLabel = new JLabel(Integer.toString(quantity));
        quantityLabel.setForeground(Color.WHITE);
        quantityLabel.setFont(FontsAndBorders.FONT_MENU_BUTTON);
        quantityLabel.setHorizontalAlignment(SwingConstants.CENTER);

        setLayout(migLayout);
        refreshLayout();
    }

    private void refreshLayout() {
        removeAll();
        final String w = "w " + MainViewsPanel.DECK_ACTION_PANEL_WIDTH + "!";
        migLayout.setLayoutConstraints("insets 0, gap 0, flowy");
        add(addButton, w);
        add(minusButton, w);
        add(incrementButton, w + ", gaptop 10");
        add(quantityLabel, w);
        add(decrementButton, w);
    }

    int getQuantity() {
        return quantity;
    }

}
