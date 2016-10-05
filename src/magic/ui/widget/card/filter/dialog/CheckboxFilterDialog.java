package magic.ui.widget.card.filter.dialog;

import magic.ui.widget.card.filter.button.CheckBoxFilterPanel;
import magic.model.MagicCardDefinition;
import magic.ui.widget.card.filter.FilterOptionsPanel;
import magic.ui.widget.card.filter.ScrollableFilterPane;

@SuppressWarnings("serial")
public class CheckboxFilterDialog extends MultiSelectFilterDialog {

    private final CheckBoxFilterPanel fbp;
    
    public CheckboxFilterDialog(final CheckBoxFilterPanel fbp, Object[] filterValues) {

        this.fbp = fbp;
        this.filterOptionsPanel = new FilterOptionsPanel(fbp);

        ScrollableFilterPane filterPanel = new ScrollableFilterPane(filterValues, fbp);

        setSize(fbp.getFilterDialogSize());

        add(filterPanel);
        add(filterOptionsPanel);

        this.filter = filterPanel;
    }

    public String getItemText(int i) {
        return filter.getItemText(i);
    }

    @Override
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        return fbp.isCardValid(card, i);
    }

}
