package magic.ui.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Random;

public class GameLoadingMessage {

    private static final String MSG[] = new String[] {
        "shuffling decks",
        "wrangling squirrels",
        "assembling Urzatron",
        "seasoning flavor text"
    };

    private static final Color COLOR1 = new Color(255, 255, 255, 0);
    private static final Color COLOR2 = new Color(255, 255, 255, 180);
    private static final Font MSGFONT = new Font("Dialog", Font.BOLD, 24);

    private boolean isEnabled = true;
    private String message;
    private long wait_time = System.currentTimeMillis();

    public GameLoadingMessage() {
        message = getRandomWaitMessage();
    }

    private String getRandomWaitMessage() {
        final Random rand = new Random();
        return "..." + MSG[rand.nextInt(MSG.length)] + "..." ;
    }

    public void render(Graphics g, Dimension container) {

        if (isEnabled == false)
            return;

        if (System.currentTimeMillis() - wait_time > 1000) {
            wait_time = System.currentTimeMillis();
            message = getRandomWaitMessage();
        }

        final Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setFont(MSGFONT);
        final FontMetrics metrics = g2d.getFontMetrics();
        final int sw = metrics.stringWidth(message);

        final int h = 40;
        final int sx = (container.width - sw) / 2;
        final int by = (container.height - h) / 2;
        final int w = container.width / 2;
        final int gw = w / 2;
        g2d.setPaint(new GradientPaint(0, 0, COLOR1, w - gw, 0, COLOR2));
        g2d.fillRect(0, by, w, h * 2);
        g2d.setPaint(new GradientPaint(container.width - gw, 0, COLOR2, container.width, 0, COLOR1));
        g2d.fillRect(w, by, w, h * 2);

        g2d.setColor(Color.DARK_GRAY);
        g2d.drawString(message, sx, by + h + 28);

        g2d.dispose();
    }

    public void setEnabled(boolean b) {
        this.isEnabled = b;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

}
