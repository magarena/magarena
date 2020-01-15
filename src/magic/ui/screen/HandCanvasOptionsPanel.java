package magic.ui.screen;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import magic.translate.MText;
import magic.ui.FontsAndBorders;
import magic.ui.screen.widget.BigDialButton;
import magic.ui.screen.widget.IDialButtonHandler;

@SuppressWarnings("serial")
public class HandCanvasOptionsPanel extends ScreenOptionsPanel
    implements IDialButtonHandler {

    // translatable UI text (prefix with _S).
    private static final String _S1 = "Layout";

    private final BigDialButton layoutButton;
    private final HandCanvasScreen screen;

    public HandCanvasOptionsPanel(final HandCanvasScreen screen) {
        this.screen = screen;
        layoutButton = new BigDialButton(this);
        setLayout();
    }

    @Override
    protected void setLayout() {
        removeAll();
        if (isMenuOpen) {
            add(getLabel(MText.get(_S1)), "ax center, w 60!");
            add(layoutButton, "ax center, h 24!, w 24!, gapbottom 2");
            add(closeButton, "spany 2, h 32!, w 32!");
        } else {
            add(menuButton, "spany 2, h 32!, w 32!");
        }
        revalidate();
        repaint();
    }

    private JLabel getLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(FontsAndBorders.FONT0);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        return lbl;
    }

    @Override
    public int getDialPositionsCount() {
        return HandZoneLayout.values().length;
    }

    @Override
    public int getDialPosition() {
        return HandZoneLayout.getLayout().ordinal();
    }

    @Override
    public boolean doLeftClickAction(int dialPosition) {
        screen.setCardsLayout(dialPosition);
        return true;
    }

    @Override
    public boolean doRightClickAction(int dialPosition) {
        screen.setCardsLayout(dialPosition);
        return true;
    }

    @Override
    public void onMouseEntered(int dialPosition) {
        screen.flashLayoutSetting();
    }
}
