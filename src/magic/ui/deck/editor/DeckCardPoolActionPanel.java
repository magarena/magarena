package magic.ui.deck.editor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import magic.data.MagicIcon;
import magic.ui.IconImages;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.widget.FontsAndBorders;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class DeckCardPoolActionPanel extends JPanel {

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

        public DeckCardPoolActionPanel(final AbstractAction plusButtonAction, final AbstractAction minusButtonAction) {

            setOpaque(false);

            addButton = new ActionBarButton(
                    IconImages.getIcon(MagicIcon.PLUS_ICON),
                    "Add card", "Add selected card to deck (or left click row).",
                    plusButtonAction);

            minusButton = new ActionBarButton(
                    IconImages.getIcon(MagicIcon.MINUS_ICON),
                    "Remove card", "Remove selected card from deck (or right click row).<br>Must use this button to remove last instance of a card.",
                    minusButtonAction);

            incrementButton = new ActionBarButton(
                    IconImages.getIcon(MagicIcon.ARROWUP_ICON),
                    "Increment",
                    "Increase the quantity by one. Maximum is ten.",
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
                    IconImages.getIcon(MagicIcon.ARROWDOWN_ICON),
                    "Decrement",
                    "Decrease the quantity by one. Minium is one.",
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
            migLayout.setLayoutConstraints("insets 0, gap 0, flowy");
            add(addButton, "w 100%, alignx center");
            add(minusButton, "w 100%, alignx center");
            add(incrementButton, "w 100%, alignx center, gaptop 10");
            add(quantityLabel, "w 100%, alignx center");
            add(decrementButton, "w 100%, alignx center");
        }

        public int getQuantity() {
            return quantity;
        }

    }
