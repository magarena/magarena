package magic.ui.widget.duel.player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.ease.Spline;

import magic.data.GeneralConfig;
import magic.data.MagicIcon;
import magic.data.settings.BooleanSetting;
import magic.ui.MagicImages;
import magic.ui.duel.viewerinfo.PlayerViewerInfo;
import magic.ui.helpers.ImageHelper;
import magic.ui.utility.MagicStyle;

@SuppressWarnings("serial")
public class PlayerImagePanel extends JPanel {

    private static final Font HEALTH_FONT = new Font("Dialog", Font.BOLD, 20);

    private final BufferedImage activeImage;
    private final Image inactiveImage;
    private PlayerViewerInfo player;
    private int life = 0;
    private int damageColorOpacity = 0;
    private int healColorOpacity = 0;

    public PlayerImagePanel(final PlayerViewerInfo player) {
        this.player = player;
        activeImage = getPlayerAvatarImage();
        inactiveImage = ImageHelper.getGreyScaleImage(activeImage);
    }

    private BufferedImage getPlayerAvatarImage() {
        return ImageHelper.scale(
            ImageHelper.getConvertedIcon(player.getPlayerPanelAvatar()),
            74, 74
        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        final Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(player.isPlayerTurn() ? activeImage : inactiveImage, 0, 0, this);

        // health
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        drawHealthValueOverlay(g2d, 0, 0, activeImage);
        // monarch
        drawMonarchOverlay(g2d);
        // Animations
        drawDamageAlert(g2d);
        drawHealAlert(g2d);
    }

    private void drawMonarchOverlay(Graphics2D g2d) {
        if (player.isMonarch()) {
            g2d.drawImage(
                MagicImages.getIcon(MagicIcon.LOYALTYCOUNTER).getImage(),
                4,
                4,
                null
            );
        }
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
        final String text = Integer.toString(player.life);
        final int textX = x + 4;
        final int textY = y + image.getHeight(null) - 6;
        ImageHelper.drawStringWithOutline(g2d, text, textX, textY);
    }

    public void updateDisplay(final PlayerViewerInfo playerInfo) {
        this.player = playerInfo;
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

    public int getDamageColorOpacity() {
        return damageColorOpacity;
    }

    public void setDamageColorOpacity(int opacity) {
        this.damageColorOpacity = opacity;
        repaint();
    }

    private void doDamageAnimation() {
        if (GeneralConfig.get(BooleanSetting.ANIMATE_GAMEPLAY)) {
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
        if (GeneralConfig.get(BooleanSetting.ANIMATE_GAMEPLAY)) {
            final Timeline timeline = new Timeline();
            timeline.setDuration(1000);
            timeline.setEase(new Spline(0.8f));
            timeline.addPropertyToInterpolate(
                    Timeline.property("healColorOpacity").on(this).from(100).to(0));
            timeline.play();
        }
    }
}
