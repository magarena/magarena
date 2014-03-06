package magic.ui.widget;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DeckComboPanel extends TexturedPanel {

    private final DecksComboBox decksComboBox;

    public DeckComboPanel(final String deckGenerator) {

        this.decksComboBox = new DecksComboBox(deckGenerator);

        setBorder(FontsAndBorders.BLACK_BORDER);
        setBackground(FontsAndBorders.MAGSCREEN_BAR_COLOR);

        setLayout(new MigLayout("center, center"));
        setOpaque(false);
        add(getDeckLabel());
        add(decksComboBox , "w 140!");

    }

    private JLabel getDeckLabel() {
        final JLabel lbl = new JLabel("Random Deck:");
        lbl.setFont(new Font("dialog", Font.PLAIN, 16));
        lbl.setForeground(Color.WHITE);
        return lbl;
    }

    /**
     * @return
     */
    public String getDeckGenerator() {
        return decksComboBox.getSelectedItem();
    }

}
