package magic.ui.widget;

import javax.swing.JComponent;
import magic.ui.helpers.MouseHelper;

@SuppressWarnings("serial")
public abstract class ChoicePanelButton extends PanelButton {

    protected boolean isValidChoice = false;

    protected abstract void setValidChoiceStyle();

    public void setIsValidChoice(boolean b) {
        isValidChoice = b;
        setValidChoiceStyle();
    }

    @Override
    public void setComponent(final JComponent component) {
        add(component, "grow");
    }

    @Override
    public void onMouseClicked() {
        MouseHelper.showDefaultCursor(this);
    }

    @Override
    protected boolean isValidClick() {
        return isValidChoice;
    }
}
