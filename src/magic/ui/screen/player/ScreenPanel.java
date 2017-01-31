package magic.ui.screen.player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import magic.ui.helpers.ImageHelper;

@SuppressWarnings("serial")
class ScreenPanel extends JPanel {

    private static final Color BCOLOR = new Color(255, 255, 255, 100);

    ScreenPanel() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setColor(BCOLOR);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setFont(getFont().deriveFont(48f));
        int w = g2d.getFontMetrics(g2d.getFont()).stringWidth("TEST");
        ImageHelper.drawStringWithOutline(g2d, "TEST", (getWidth() -  w) / 2, (getHeight() - g2d.getFontMetrics().getHeight()) / 2);
        g2d.dispose();
    }
}
