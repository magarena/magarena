package magic.ui.explorer.filter.dialogs;

import magic.model.MagicCardDefinition;
import magic.ui.explorer.filter.ColorsFilterPanel;
import magic.ui.explorer.filter.FilterOptionsPanel;
import magic.ui.explorer.filter.buttons.ColorFilterPanel;

@SuppressWarnings("serial")
public class ColorFilterDialog extends MultiSelectFilterDialog {

    private final ColorFilterPanel fbp;

    public ColorFilterDialog(final ColorFilterPanel fbp, Object[] filterValues) {

        this.fbp = fbp;
        this.filterOptionsPanel = new FilterOptionsPanel(fbp);

        ColorsFilterPanel filterPanel = new ColorsFilterPanel(fbp);

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
