package magic.ui.widget.card.filter.dialog;

import magic.model.MagicCardDefinition;
import magic.ui.widget.card.filter.ColorValuesPanel;
import magic.ui.widget.card.filter.FilterOptionsPanel;
import magic.ui.widget.card.filter.button.ColorFilterPanel;

@SuppressWarnings("serial")
public class ColorFilterDialog extends MultiSelectFilterDialog {

    private final ColorFilterPanel fbp;

    public ColorFilterDialog(final ColorFilterPanel fbp, Object[] filterValues) {

        this.fbp = fbp;
        this.filterOptionsPanel = new FilterOptionsPanel(fbp);

        ColorValuesPanel filterPanel = new ColorValuesPanel(fbp);

        setSize(fbp.getFilterDialogSize());

        add(filterPanel);
        add(filterOptionsPanel);

        this.filter = filterPanel;
    }

    @Override
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        return fbp.isCardValid(card, i);
    }


}
