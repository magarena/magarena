package magic.ui.widget;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.duel.animation.AnimationFx;
import magic.ui.widget.duel.animation.MagicAnimations;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.ease.Spline;

@SuppressWarnings("serial")
public abstract class ChoiceBorderPanelButton extends ChoicePanelButton {

    private static final int CHOICE_BORDER_MAX_OPACITY = 200;
    private static final int CHOICE_BORDER_WIDTH = 6;
    private static final int CHOICE_BORDER_OFFSET = CHOICE_BORDER_WIDTH / 2;
    private static final Stroke CHOICE_BORDER_STROKE = new BasicStroke(CHOICE_BORDER_WIDTH);
    private static final Color CHOICE_BORDER_COLOR =
        ThemeFactory.getInstance().getCurrentTheme().getColor(Theme.COLOR_CHOICE_BORDER);

    private int choiceBorderOpacity = 0;
    private final Timeline pulseBorderTimeline = new Timeline();

    private void showSolidChoiceBorder(boolean b) {
        setChoiceBorderOpacity(b ? CHOICE_BORDER_MAX_OPACITY : 0);
        repaint();
    }

    @Override
    protected void setValidChoiceStyle() {
        if (MagicAnimations.isOn(AnimationFx.AVATAR_PULSE)) {
            showPulsingChoiceBorder(isValidChoice);
        } else {
            showSolidChoiceBorder(isValidChoice);
        }
    }

    // Must be public for timeline to work.
    public int getChoiceBorderOpacity() {
        return choiceBorderOpacity;
    }

    // Must be public for timeline to work.
    public void setChoiceBorderOpacity(int pulsingBorderOpacity) {
        this.choiceBorderOpacity = pulsingBorderOpacity;
        repaint();
    }

    private void showPulsingChoiceBorder(boolean b) {
        if (b) {
            startPulsingBorderAnimation();
        } else {
            stopPulsingBorderAnimation();
        }
    }

    private void startPulsingBorderAnimation() {
        if (MagicAnimations.isOn(AnimationFx.AVATAR_PULSE)) {
            stopPulsingBorderAnimation();
            pulseBorderTimeline.setDuration(500);
            pulseBorderTimeline.setEase(new Spline(0.8f));
            pulseBorderTimeline.addPropertyToInterpolate(
                    Timeline.property("ChoiceBorderOpacity").on(this)
                        .from(20).to(CHOICE_BORDER_MAX_OPACITY)
            );
            pulseBorderTimeline.playLoop(Timeline.RepeatBehavior.REVERSE);
        }
    }

    private boolean isTimelineRunning() {
        return pulseBorderTimeline.getState() != Timeline.TimelineState.IDLE;
    }

    private void stopPulsingBorderAnimation() {
        if (pulseBorderTimeline != null && isTimelineRunning()) {
            pulseBorderTimeline.abort();
            setChoiceBorderOpacity(0);
        }
    }

    private void drawChoiceBorder(Graphics2D g2d) {
        g2d.setStroke(CHOICE_BORDER_STROKE);
        g2d.setColor(MagicStyle.getTranslucentColor(
            CHOICE_BORDER_COLOR, getChoiceBorderOpacity())
        );
        g2d.drawRect(
            CHOICE_BORDER_OFFSET,
            CHOICE_BORDER_OFFSET,
            getWidth() - CHOICE_BORDER_WIDTH - 1,
            getHeight() - CHOICE_BORDER_WIDTH - 1);
    }

    private boolean canDrawChoiceBorder() {
        return isValidChoice && choiceBorderOpacity > 0;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (canDrawChoiceBorder()) {
            drawChoiceBorder((Graphics2D) g);
        }
    }

}
