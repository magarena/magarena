package magic.ui.widget.card.filter;

import magic.ui.mwidgets.MCheckBox;

@SuppressWarnings("serial")
class FilterCheckBox extends MCheckBox
    implements IFilterCheckBox {

    FilterCheckBox(String text) {
        super(text);
    }

    @Override
    public boolean isSelected() {
        return super.isSelected();
    }

    @Override
    public void setSelected(boolean b) {
        super.setSelected(b);
    }

    @Override
    public String getText() {
        return super.getText();
    }
}
