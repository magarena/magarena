package magic.ui.explorer.filter;

@SuppressWarnings("serial")
class TextFilterDialog extends FilterDialog {

    private final CardFilterTextField nameTextField;

    TextFilterDialog(final FilterButtonPanel fbp, CardFilterTextField f) {
        this.nameTextField = f;
        setSize(fbp.getFilterDialogSize());
        setLayout(fbp.getFilterDialogLayout());
        add(nameTextField);
    }

    @Override
    void reset() {
        nameTextField.setText("");
    }

}
