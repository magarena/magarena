package magic.ui.screen.cardflow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Timer;
import magic.awt.MagicFont;
import magic.ui.helpers.ImageHelper;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.TexturedPanel;

@SuppressWarnings("serial")
public class FlashTextOverlay extends TexturedPanel {

    private String text;
    private Font textFont;

    private final Timer timer =
        new Timer(1200, (e) -> {setVisible(false); });

    public FlashTextOverlay() {

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) { setVisible(false); }
            @Override
            public void mouseEntered(MouseEvent e) { setVisible(false); }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent event) {
                textFont = MagicFont.BelerenBold.get().deriveFont(getHeight()-6f);
            }
        });

        setPreferredSize(new Dimension(400, 60));

        setBackground(MagicStyle.getTranslucentColor(Color.DARK_GRAY, 200));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        setVisible(false);
    }

    public void flashText(String text) {
        this.text = text;
        timer.restart();
        setVisible(true);
    }

    private void paintFlashText(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setFont(textFont);
        int w = g2d.getFontMetrics(textFont).stringWidth(text);
        ImageHelper.drawStringWithOutline(g2d, text, (getWidth() - w) / 2, getHeight() - 10);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (text != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            paintFlashText(g2d);
            g2d.dispose();
        }
    }

}
