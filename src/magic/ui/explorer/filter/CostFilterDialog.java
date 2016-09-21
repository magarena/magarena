package magic.ui.explorer.filter;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class CostFilterDialog extends CheckboxFilterDialog {

    CostFilterDialog(final FilterButtonPanel fbp, Object[] filterValues) {
        super(fbp, filterValues);
        this.filterPane.setMigLayout(new MigLayout("flowx, wrap 6, insets 2, gap 8"));
    }
}
