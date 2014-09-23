package magic.ui.explorer;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import magic.data.IconImages;
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
                    IconImages.MISSING_ICON, // PLUS28_ICON,
                    "Add Card(s)", "Add selected cards to deck",
                    plusButtonAction);

            minusButton = new ActionBarButton(
                    IconImages.MISSING_ICON, // MINUS28_ICON,
                    "Remove Card(s)", "Remove selected cards from deck",
                    minusButtonAction);

            incrementButton = new ActionBarButton(
                    IconImages.MISSING_ICON, // ARROWUP_ICON,
                    "Increment Quantity",
                    "Increase the quantity by one. Maximum is ten.",
                    new AbstractAction() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            quantity++;
                            if (quantity > max_quantity) {
                                quantity = max_quantity;
                            }
//                            addButton.setText("+" + quantity);
//                            minusButton.setText("-" + quantity);
                            quantityLabel.setText(Integer.toString(quantity));
                        }
                    });
            decrementButton = new ActionBarButton(
                    IconImages.MISSING_ICON, // ARROWDOWN_ICON,
                    "Decrement Quantity",
                    "Decrease the quantity by one. Minium is one.",
                    new AbstractAction() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            quantity--;
                            if (quantity < 1) {
                                quantity = 1;
                            }
//                            addButton.setText("+" + quantity);
//                            minusButton.setText("-" + quantity);
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
