package magic.ui.widget.duel.animation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import javax.swing.SwingUtilities;
import magic.data.GeneralConfig;
import magic.ui.duel.viewerinfo.GameViewerInfo;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.TimelineScenario;
import org.pushingpixels.trident.callback.TimelineCallback;

class NewTurnAnimation extends MagicAnimation {

    private final Timeline timelines[] = new Timeline[1];
    private TimelineScenario scenario;
    private final GameLayoutInfo layoutInfo;
    private final GameViewerInfo gameInfo;

    public NewTurnAnimation(GameViewerInfo newGameInfo, GameLayoutInfo layoutInfo) {
        this.layoutInfo = layoutInfo;
        this.gameInfo = newGameInfo;
    }

    @Override
    protected void render(Graphics g) {

        final Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND));
        final Rectangle rect = layoutInfo.getTurnPanelLayout();
        g2d.fillRect(rect.x, rect.y, rect.width, rect.height);

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        final Font f = g2d.getFont().deriveFont(28f);
        g2d.setFont(f);
        final String s = "Turn " + gameInfo.getTurn();
        int w = g2d.getFontMetrics(f).stringWidth(s);
        int h = g2d.getFontMetrics(f).getAscent(); // .getHeight();
        final Rectangle2D test = g2d.getFontMetrics(f).getStringBounds(s, g);
        int x = (rect.width / 2) - (w / 2);
        g2d.drawString(s, x, rect.y + h + ((rect.height - h) / 2));
//        g2d.drawRect(x, rect.y + ((rect.height - h) / 2), (int)test.getWidth(), (int)test.getHeight());
        g2d.dispose();
    }

    @Override
    protected void play() {
        assert SwingUtilities.isEventDispatchThread();
        isRunning.set(true);
        setupTimelineScenario();
        scenario.play();
    }

    private Timeline getPauseTimeline(final int duration) {
        final Timeline timeline = new Timeline(this);
        timeline.setDuration(duration);
        timeline.addCallback(new TimelineCallback() {
            @Override
            public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
                if (newState == Timeline.TimelineState.PLAYING_FORWARD) {
                    getCanvas().repaint();
                }
            }
            @Override
            public void onTimelinePulse(float durationFraction, float timelinePosition) {
                // do nothing.
            }
        });
        return timeline;
    }

    private void setupTimelineScenario() {

        timelines[0] = getPauseTimeline(GeneralConfig.getInstance().getNewTurnAlertDuration());

        scenario = new TimelineScenario.Sequence();
        for (Timeline timeline : timelines) {
            scenario.addScenarioActor(timeline);
        }

        scenario.addCallback(() -> {
            SwingUtilities.invokeLater(() -> {
                getCanvas().setVisible(false);
            });
            isRunning.set(false);
        });

    }

    private void cancelAnimation() {
        if (scenario != null && scenario.getState() == TimelineScenario.TimelineScenarioState.PLAYING) {
            scenario.cancel();
        }
    }

    @Override
    protected void cancel() {
        cancelAnimation();
    }

    @Override
    protected void doCancelAction() {
        cancelAnimation();
    }

}
