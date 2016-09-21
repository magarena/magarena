package magic.ui.explorer.filter.dialogs;

import magic.ui.explorer.filter.CardFilterTextField;
import magic.ui.explorer.filter.buttons.FilterPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class TextFilterDialog extends FilterDialog {

    private final CardFilterTextField nameTextField;

    public TextFilterDialog(final FilterPanel fbp, CardFilterTextField f) {
        this.nameTextField = f;
        setSize(fbp.getFilterDialogSize());
        setLayout(new MigLayout("flowy, gap 0, insets 0", "[fill, grow]", "[fill, grow]"));
        add(nameTextField);
    }

    @Override
    public void reset() {
        nameTextField.setText("");
    }

    @Override
    public boolean isFiltering() {
        return nameTextField.getText().trim().isEmpty() == false;
    }
}
