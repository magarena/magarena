package magic.ui.duel.player;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import magic.data.GeneralConfig;
import magic.ui.MagicStyle;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.ease.Spline;

@SuppressWarnings("serial")
public class AnimationPanel extends JPanel {

    private static final int PULSE_BORDER_WIDTH = 5;
    
    private int pulsingBorderOpacity = 0;
    private final Timeline pulseBorderTimeline = new Timeline();

    public int getPulsingBorderOpacity() {
        return pulsingBorderOpacity;
    }

    public void setPulsingBorderOpacity(int pulsingBorderOpacity) {
        this.pulsingBorderOpacity = pulsingBorderOpacity;
        repaint();
    }

    public void doPulsingBorderAnimation(boolean b) {
        if (b) {
            startPulsingBorderAnimation();
        } else {
            stopPulsingBorderAnimation();
        }
    }

    private void startPulsingBorderAnimation() {
        if (GeneralConfig.getInstance().isAnimateGameplay()) {
            stopPulsingBorderAnimation();
            pulseBorderTimeline.setDuration(500);
            pulseBorderTimeline.setEase(new Spline(0.8f));
            pulseBorderTimeline.addPropertyToInterpolate(
                    Timeline.property("pulsingBorderOpacity").on(this).from(20).to(200));
            pulseBorderTimeline.playLoop(Timeline.RepeatBehavior.REVERSE);
        }
    }

    private void stopPulsingBorderAnimation() {
        if (pulseBorderTimeline != null && pulseBorderTimeline.getState() != Timeline.TimelineState.IDLE) {
            pulseBorderTimeline.abort();
            setPulsingBorderOpacity(0);
        }
    }

    protected void drawPulsingBorder(Graphics2D g) {
        if (pulsingBorderOpacity > 0) {
            final int offset = (int)(PULSE_BORDER_WIDTH / 2);
            final Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(PULSE_BORDER_WIDTH));
            g2d.setColor(MagicStyle.getTranslucentColor(ThemeFactory.getInstance().getCurrentTheme().getColor(Theme.COLOR_CHOICE_BORDER), getPulsingBorderOpacity()));
            g2d.drawRect(
                    0 + offset,
                    0 + offset,
                    getWidth() - PULSE_BORDER_WIDTH - 1,
                    getHeight() - PULSE_BORDER_WIDTH - 1);
        }
    }

}
