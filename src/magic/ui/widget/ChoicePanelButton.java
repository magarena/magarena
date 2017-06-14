package magic.ui.widget;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import magic.ui.helpers.MouseHelper;

@SuppressWarnings("serial")
public abstract class ChoicePanelButton extends PanelButton {

    protected boolean isValidChoice = false;

    protected abstract void setValidChoiceStyle();

    public ChoicePanelButton() {

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent event) {
                if (isValidChoice) {
                    MouseHelper.showHandCursor(ChoicePanelButton.this);
                }
            }
            @Override
            public void mouseExited(final MouseEvent event) {
                MouseHelper.showDefaultCursor(ChoicePanelButton.this);
            }
        });
    }

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
