package magic.ui.duel.animation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.SwingUtilities;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.model.MagicPlayer;
import magic.ui.CachedImagesProvider;
import magic.ui.MagicImages;
import magic.ui.duel.viewer.info.CardViewerInfo;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.TimelineScenario;
import org.pushingpixels.trident.callback.TimelineCallbackAdapter;
import org.pushingpixels.trident.ease.Spline;

abstract class CardAnimation extends MagicAnimation {

    protected static final int GROW_DURATION = 800;   // milliseconds
    protected static final int FLIP_DURATION = 400;   // milliseconds
    protected static final int SHRINK_DURATION = 400; // milliseconds

    protected static final Color SHADOW_COLOR = new Color(0, 0, 0, 100);

    private static final ImageScaler backImageScaler = new ImageScaler(MagicImages.BACK_IMAGE);

    private Timeline growTimeline;
    private Timeline flipTimeline;
    private Timeline shrinkTimeline;
    private Timeline viewTimeline;
    private TimelineScenario.RendezvousSequence scenario;

    private volatile float flipPosition = 1f;

    private final GameLayoutInfo layoutInfo;
    private final int playerIndex;
    private ImageScaler imageScaler;
    private Dimension previewSize;
    private Rectangle imageRect = new Rectangle();
    private final MagicPlayer player;
    private final CardViewerInfo cardInfo;

    // abstract methods
    protected abstract Rectangle getStart();
    protected abstract Rectangle getEnd();

    CardAnimation(MagicPlayer aPlayer, CardViewerInfo cardInfo, GameLayoutInfo layoutInfo) {
        this.player = aPlayer;
        this.cardInfo = cardInfo;
        this.playerIndex = aPlayer.getIndex();
        this.layoutInfo = layoutInfo;
    }

    protected MagicPlayer getPlayer() {
        return player;
    }

    protected MagicCardDefinition getCard() {
        return cardInfo.getCardDefinition();
    }

    private void drawCardShadow(Graphics g) {
        if (MagicAnimations.isOn(AnimationFx.CARD_SHADOW)) {
            final int SHADOW = 6;
            g.setColor(SHADOW_COLOR);
            g.fillRect(imageRect.x + SHADOW,
                imageRect.y + SHADOW,
                imageScaler.getImage().getWidth(null),
                imageScaler.getImage().getHeight(null)
            );
        }
    }

    private boolean isCardFlipping() {
        return MagicAnimations.isOn(AnimationFx.FLIP_CARD) && flipPosition < 1f;
    }

    private boolean isViewTimelineRunning() {
        return viewTimeline != null && viewTimeline.getState() == Timeline.TimelineState.PLAYING_FORWARD;
    }

    private void drawArrow(Graphics g) {
        if (MagicAnimations.isOn(AnimationFx.FROM_ARROW) && isViewTimelineRunning()) {
            ArrowBuilder.drawArrow(g, getStart(), getPreviewRectangle());
        }
    }

    private void drawCardImage(Graphics g) {
        if (isCardFlipping()) {
            final int w = Math.abs((int)(Math.cos(flipPosition * Math.PI) * imageRect.width));
            g.drawImage(
                flipPosition > 0.5f ? imageScaler.getImage() : backImageScaler.getImage(),
                imageRect.x + ((imageRect.width - w) / 2),
                imageRect.y,
                w,
                imageRect.height,
                getCanvas()
            );
        } else {
            g.drawImage(
                imageScaler.getImage(),
                imageRect.x,
                imageRect.y,
                getCanvas()
            );
        }
    }

    @Override
    protected void render(Graphics g) {

        if (imageRect.isEmpty())
            return;

        if (growTimeline.isDone()) {
            drawArrow(g);
            drawCardShadow(g);
        }

        drawCardImage(g);
        
    }

    private Dimension getCardPreviewSize(Image image) {
        final Dimension prefSize = MagicImages.getPreferredImageSize(image);
        final Dimension container = layoutInfo.getGamePanelSize();
        if (container.height < prefSize.height) {
            final int newWidth = (int)((container.height / (double)prefSize.height) * prefSize.width);
            return new Dimension(newWidth, container.height);
        } else {
            return prefSize;
        }
    }
    
    /**
     * Returns the dimensions and position of the full size card image.
     */
    private Rectangle getPreviewRectangle() {
        final int x = (layoutInfo.getGamePanelSize().width - previewSize.width) / 2;
        final int y = (layoutInfo.getGamePanelSize().height - previewSize.height) / 2;
        return new Rectangle(new Point(x, y), previewSize);
    }

    /**
    * This is called by a timeline (do not delete!).
    */
    public void setGrowRectangle(final Rectangle rect) {
        assert !SwingUtilities.isEventDispatchThread();
        imageRect = rect;
        imageScaler.setSize(rect.getSize(), growTimeline.getTimelinePosition() > 0.96f);
        getCanvas().repaint();
    }

    private Timeline getGrowTimeline() {
        final Timeline timeline = new Timeline(this);
        timeline.addPropertyToInterpolate("GrowRectangle", getStart(), getPreviewRectangle());
        timeline.setDuration(GROW_DURATION);
        timeline.setEase(new Spline(0.8f));
        return timeline;
    }

    /**
    * This is called by a timeline (do not delete!).
    */
    public void setFlipPosition(final float value) {
        assert !SwingUtilities.isEventDispatchThread();
        flipPosition = value;
    }

    private Timeline getFlipTimeline() {
        final Timeline timeline = new Timeline(this);
        timeline.addPropertyToInterpolate("FlipPosition", 0.0f, 1.0f);
        timeline.setDuration(FLIP_DURATION);
        timeline.setInitialDelay(GROW_DURATION - FLIP_DURATION);
        return timeline;
    }

    /**
    * This is called by a timeline (do not delete!).
    */    
    public void setShrinkRectangle(final Rectangle rect) {
        assert !SwingUtilities.isEventDispatchThread();
        imageRect = rect;
        imageScaler.setLQSize(rect.getSize());
        getCanvas().repaint();
    }

    private Timeline getShrinkTimeline() {
        final Timeline timeline = new Timeline(this);
        timeline.addPropertyToInterpolate("ShrinkRectangle", getPreviewRectangle(), getEnd());
        timeline.setDuration(SHRINK_DURATION);
        timeline.setEase(new Spline(0.8f));
        timeline.addCallback(new TimelineCallbackAdapter() {
            @Override
            public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
                if (newState == Timeline.TimelineState.DONE) {
                    scenario.cancel();
                }
            }
        });
        return timeline;
    }

    /**
     * Amount of time in millisecs that full-size card preview is displayed.
     */
    private int getViewDuration() {
        return cardInfo.isLand()
            ? GeneralConfig.getInstance().getLandPreviewDuration()
            : GeneralConfig.getInstance().getNonLandPreviewDuration();
    }

    private Timeline getViewTimeline() {
        final Timeline timeline = new Timeline();
        timeline.setDuration(getViewDuration());
        timeline.addCallback(new TimelineCallbackAdapter() {
            @Override
            public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
                if (newState == Timeline.TimelineState.PLAYING_FORWARD) {
                    // ensures arrow is painted correctly.
                    getCanvas().repaint();
                }
            }
        });
        return timeline;
    }
    
    private void playTimelineScenario() {

        growTimeline = getGrowTimeline();
        flipTimeline = getFlipTimeline();
        viewTimeline = getViewTimeline();
        shrinkTimeline = getShrinkTimeline();

        scenario = new TimelineScenario.RendezvousSequence();        
        scenario.addScenarioActor(growTimeline);
        scenario.addScenarioActor(flipTimeline);
        scenario.rendezvous();
        scenario.addScenarioActor(viewTimeline);
        scenario.rendezvous();
        scenario.addScenarioActor(shrinkTimeline);
        
        scenario.addCallback(() -> {
            SwingUtilities.invokeLater(() -> {
                imageRect = new Rectangle();
                getCanvas().setVisible(false);
            });
            isRunning.set(false);
        });

        scenario.play();
    }

    @Override
    protected void play() {

        assert SwingUtilities.isEventDispatchThread();
        isRunning.set(true);

        flipPosition = 0f;
        imageScaler = new ImageScaler(CachedImagesProvider.getInstance().getImage(cardInfo.getCardDefinition(), 0, true));
        this.previewSize = getCardPreviewSize(imageScaler.getImage());

        playTimelineScenario();
    }

    @Override
    protected void cancel() {
        if (scenario != null && scenario.getState() == TimelineScenario.TimelineScenarioState.PLAYING) {
            scenario.cancel();
        }
    }

    @Override
    protected void doCancelAction() {
        if (viewTimeline.getState() == Timeline.TimelineState.PLAYING_FORWARD) {
            viewTimeline.cancel();
            shrinkTimeline.play();
        }
    }

    protected int getPlayerIndex() {
        return playerIndex;
    }

    protected GameLayoutInfo getLayoutInfo() {
        return layoutInfo;
    }

}


