package magic.ui.duel.player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import magic.data.GeneralConfig;
import magic.model.MagicGame;
import magic.ui.GraphicsUtilities;
import magic.ui.IconImages;
import magic.ui.MagicStyle;
import magic.ui.duel.CounterOverlay;
import magic.ui.duel.viewer.PlayerViewerInfo;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.ease.Spline;

@SuppressWarnings("serial")
public class PlayerImagePanel extends JPanel {

    private static final Font HEALTH_FONT = new Font("Dialog", Font.BOLD, 20);

    private final MagicGame game;
    private final CounterOverlay poisonCounter;
    private final CounterOverlay damageCounter;    
    private final BufferedImage activeImage;
    private final BufferedImage inactiveImage;
    private PlayerViewerInfo playerInfo;
    private int life = 0;
    private int damageColorOpacity = 0;
    private int healColorOpacity = 0;

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
        } else {
            g2d.drawImage(inactiveImage, 0, 0, this);
        }

        // counters
        drawPoisonCountersOverlay(g2d);
        drawShieldDamageOverlay(g2d);
        // health
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        drawHealthValueOverlay(g2d, 0, 0, activeImage);

        // Animations
        drawDamageAlert(g2d);
        drawHealAlert(g2d);

    }

    private void drawDamageAlert(Graphics2D g2d) {
        if (damageColorOpacity > 0) {
            g2d.setColor(MagicStyle.getTranslucentColor(Color.RED, damageColorOpacity));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private void drawHealAlert(Graphics2D g2d) {
        if (healColorOpacity > 0) {
            g2d.setColor(MagicStyle.getTranslucentColor(Color.GREEN, healColorOpacity));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
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
        final boolean isDamage = life > playerInfo.life;
        final boolean isHeal = life < playerInfo.life && life > 0;
        life = playerInfo.life;
        if (isDamage) {
            doDamageAnimation();
        } else if (isHeal) {
            doHealAnimation();
        } else {
            repaint();
        }
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

    public int getDamageColorOpacity() {
        return damageColorOpacity;
    }

    public void setDamageColorOpacity(int opacity) {
        this.damageColorOpacity = opacity;
        repaint();
    }

    private void doDamageAnimation() {
        if (GeneralConfig.getInstance().isAnimateGameplay()) {
            final Timeline timeline = new Timeline();
            timeline.setDuration(100);
            timeline.addPropertyToInterpolate(
                    Timeline.property("damageColorOpacity").on(this).from(0).to(120));
            timeline.playLoop(6, Timeline.RepeatBehavior.REVERSE);
        }
    }

    public int getHealColorOpacity() {
        return healColorOpacity;
    }

    public void setHealColorOpacity(int opacity) {
        this.healColorOpacity = opacity;
        repaint();
    }

    private void doHealAnimation() {
        if (GeneralConfig.getInstance().isAnimateGameplay()) {
            final Timeline timeline = new Timeline();
            timeline.setDuration(1000);
            timeline.setEase(new Spline(0.8f));
            timeline.addPropertyToInterpolate(
                    Timeline.property("healColorOpacity").on(this).from(100).to(0));
            timeline.play();
        }
    }

}
