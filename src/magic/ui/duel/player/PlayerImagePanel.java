package magic.ui.duel.player;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import magic.model.MagicGame;
import magic.ui.GraphicsUtilities;
import magic.ui.IconImages;
import magic.ui.duel.CounterOverlay;
import magic.ui.duel.viewer.PlayerViewerInfo;

@SuppressWarnings("serial")
public class PlayerImagePanel extends JPanel {

    private static final Font HEALTH_FONT = new Font("Dialog", Font.BOLD, 20);

    private final MagicGame game;
    private final CounterOverlay poisonCounter;
    private final CounterOverlay damageCounter;    
    private final BufferedImage activeImage;
    private final BufferedImage inactiveImage;
    private PlayerViewerInfo playerInfo;

    public PlayerImagePanel(final PlayerViewerInfo player, final MagicGame game) {
        this.playerInfo = player;
        this.game = game;
        activeImage = getPlayerAvatarImage();
        inactiveImage = GraphicsUtilities.getGreyScaleImage(activeImage);
        poisonCounter = new CounterOverlay(20, 20, Color.GREEN);
        damageCounter = new CounterOverlay(20, 20, Color.CYAN);
    }

    private BufferedImage getPlayerAvatarImage() {
        final ImageIcon icon = IconImages.getIconSize3(this.playerInfo.player.getPlayerDefinition());
        return GraphicsUtilities.scale(GraphicsUtilities.getConvertedIcon(icon), 74, 74);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        final Graphics2D g2d = (Graphics2D) g;

        if (playerInfo.player == game.getTurnPlayer()) {
            g2d.drawImage(activeImage, 0, 0, this);
//            drawPlayerHighlight(g2d);
        } else {
            g2d.drawImage(inactiveImage, 0, 0, this);
        }
                
        // counters
        drawPoisonCountersOverlay(g2d);
        drawShieldDamageOverlay(g2d);
        // health
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        drawHealthValueOverlay(g2d, 0, 0, activeImage);
    }

    private void drawPlayerHighlight(final Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(6.0f));
        g2d.setColor(new Color(255, 255, 0, 220));
        g2d.drawRect(0, 0, getWidth(), getHeight());
    }

    private void drawHealthValueOverlay(final Graphics2D g2d, final int x, final int y, final Image image) {
        g2d.setFont(HEALTH_FONT);
        final String text = Integer.toString(playerInfo.life);
        final int textX = x + 4;
        final int textY = y + image.getHeight(null) - 6;
        GraphicsUtilities.drawStringWithOutline(g2d, text, textX, textY);
    }    

    public void updateDisplay(final PlayerViewerInfo playerInfo) {
        this.playerInfo = playerInfo;
        repaint();
    }

    private void drawPoisonCountersOverlay(final Graphics2D g2d) {
        if (playerInfo.poison > 0) {
            poisonCounter.setCounterValue(playerInfo.poison);
            final BufferedImage counterImage = poisonCounter.getCounterImage();
            g2d.drawImage(
                    counterImage,
                    getWidth() - counterImage.getWidth() - 4,
                    getHeight() - counterImage.getHeight() - 4,
                    null);
        }
    }

    private void drawShieldDamageOverlay(final Graphics2D g2d) {
        if (playerInfo.preventDamage > 0) {
            damageCounter.setCounterValue(playerInfo.preventDamage);
            final BufferedImage counterImage = damageCounter.getCounterImage();
            g2d.drawImage(
                    counterImage,
                    getWidth() - counterImage.getWidth() - 4,
                    4,
                    null);
        }

    }

//    private void drawHealthGauge(final Graphics2D g2d) {
//        g2d.setStroke(new BasicStroke(1));
//        g2d.setColor(Color.BLACK);
//        final int w = 10;
//        g2d.drawRect(avatarX-w, avatarY, w, avatarImage.getHeight(null) - 1);
//        final Paint borderColor = new GradientPaint(avatarX-w, avatarY, Color.GREEN, avatarX-w, avatarY + avatarImage.getHeight(null), Color.RED, false);
//        g2d.setPaint(borderColor);
//        g2d.fillRect(avatarX-w+1, avatarY+1, w-1, avatarImage.getHeight(null) - 2);
//    }


}
