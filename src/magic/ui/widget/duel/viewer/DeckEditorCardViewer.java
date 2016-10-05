package magic.ui.widget.duel.viewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

@SuppressWarnings("serial")
public class DeckEditorCardViewer extends CardViewer {

    private final static Dimension COUNTER_SIZE = new Dimension(100, 461);
    private int cardCount = 0;

    public void setCardCount(final int cardCount) {
        this.cardCount = cardCount;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        final int x = (getWidth() - COUNTER_SIZE.width) / 2;
        drawCardCount(g, x, 30, COUNTER_SIZE.width, COUNTER_SIZE.height);
    }

    private void drawCardCount(Graphics g, int X, int Y, int W, int H) {
        if (cardCount > 0) {
            Graphics2D g2d = (Graphics2D)g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//            g2d.setColor(Color.WHITE);
            final String text = Integer.toString(cardCount);
            int h = (int)(H * 0.15);
            h = h > 8 ? (int)(H * 0.15) : 9;
            final Font f = new Font("Dialog", Font.BOLD, h);
            final int w = g2d.getFontMetrics(f).stringWidth(text);
            g2d.setFont(f);
            drawStringWithOutline(g2d, text, X + ((W - w) / 2), Y + ((H - h) / 3));
        }
    }

    private static final Color OUTLINE_COLOR = Color.DARK_GRAY;
    private static final Color FOREGROUND_COLOR = Color.YELLOW;

    private void drawStringWithOutline(final Graphics2D g2d, final String str, int x, int y) {
//        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(OUTLINE_COLOR);
        for (int i = 1; i <= 2; i++) {
            g2d.drawString(str,x+i,y);
            g2d.drawString(str,x-i,y);
            g2d.drawString(str,x,y+i);
            g2d.drawString(str,x,y-i);
        }
        g2d.setColor(FOREGROUND_COLOR);
        g2d.drawString(str,x,y);
    }

}
