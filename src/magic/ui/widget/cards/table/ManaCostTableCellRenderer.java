package magic.ui.widget.cards.table;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import magic.model.MagicCardDefinition;
import magic.model.MagicManaCost;
import magic.ui.widget.CostPanel;

@SuppressWarnings("serial")
class ManaCostTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        final MagicCardDefinition card = ((DeckTableModel) table.getModel()).getCardDef(row);
        final CostPanel myRender = new CostPanel(card.isLand() || !card.isValid() ? null : (MagicManaCost) value);

        // match border and background formatting with default
        final JComponent defaultRender = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

        myRender.setOpaque(defaultRender.isOpaque());
        myRender.setBorder(defaultRender.getBorder());

        if (isSelected) {
            myRender.setForeground(table.getSelectionForeground());
            myRender.setBackground(table.getSelectionBackground());
        } else {
            myRender.setForeground(getForeground());
                // We have to create a new color object because Nimbus returns
            // a color of type DerivedColor, which behaves strange, not sure
            // why.
            myRender.setBackground(new Color(getBackground().getRed(), getBackground().getGreen(), getBackground().getBlue()));

        }
        myRender.setBorder(noFocusBorder);
        return myRender;
    }
}
