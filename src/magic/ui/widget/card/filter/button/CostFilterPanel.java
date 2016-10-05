package magic.ui.widget.card.filter.button;

import java.awt.Dimension;
import java.awt.LayoutManager;
import magic.model.MagicCardDefinition;
import magic.model.MagicManaCost;
import magic.translate.UiString;
import magic.ui.widget.card.filter.dialog.CheckboxFilterDialog;
import magic.ui.widget.card.filter.IFilterListener;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class CostFilterPanel extends CheckBoxFilterPanel {

    // translatable strings
    private static final String _S12 = "Cost";

    public CostFilterPanel(IFilterListener aListener) {
        super(UiString.get(_S12), aListener);
    }

    @Override
    public Dimension getFilterDialogSize() {
        return new Dimension(290, 140);
    }

    @Override
    public boolean hideSearchOperandAND() {
        return true;
    }

    @Override
    public boolean isCardValid(MagicCardDefinition card, int i) {
        return card.hasConvertedCost(Integer.parseInt(values[i]));
    }

    @Override
    protected String[] getFilterValues() {
        values =  new String[MagicManaCost.MAXIMUM_MANA_COST + 1];
        for (int i = 0; i <= MagicManaCost.MAXIMUM_MANA_COST; i++) {
            values[i] = Integer.toString(i);
        }
        return values;
    }

    @Override
    protected CheckboxFilterDialog getCheckBoxFilterDialog() {
        return new CheckboxFilterDialog(this, values);
    }

    @Override
    public LayoutManager getFilterPanelLayout() {
        return new MigLayout("flowx, wrap 6, insets 2, gap 8");
    }        
}
