package magic.ui.widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.utility.MagicStyle;

@SuppressWarnings("serial")
public abstract class ChoiceOverlayPanelButton extends ChoicePanelButton {

    private static final int CHOICE_COLOR_OPACITY = 60;
    private static final Color CHOICE_COLOR =
        ThemeFactory.getInstance().getCurrentTheme().getColor(Theme.COLOR_CHOICE_BORDER);

    @Override
    protected void setValidChoiceStyle() {
        repaint();
    }

    private void drawChoiceOverlay(Graphics2D g2d) {
        g2d.setColor(MagicStyle.getTranslucentColor(CHOICE_COLOR, CHOICE_COLOR_OPACITY));
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (isValidChoice) {
            drawChoiceOverlay((Graphics2D) g);
        }
    }

}
