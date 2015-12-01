package magic.ui.duel.animation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.SwingUtilities;
import magic.data.GeneralConfig;
import magic.data.MagicIcon;
import magic.model.MagicCardDefinition;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.ui.CachedImagesProvider;
import magic.ui.CardImagesProvider;
import magic.ui.MagicImages;
import magic.ui.utility.GraphicsUtils;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.TimelineScenario;
import org.pushingpixels.trident.callback.TimelineCallbackAdapter;
import org.pushingpixels.trident.ease.Spline;

abstract class CardAnimation extends MagicAnimation {

    protected static final int ZOOM_DURATION = 800; // milliseconds
    protected static final Color SHADOW_COLOR = new Color(0, 0, 0, 100);

    private static final ImageScaler backImageScaler;
    static {
        backImageScaler = new ImageScaler(MagicImages.BACK_IMAGE);
    }

    private static final ImageScaler zoneImageScaler;
    static {
        zoneImageScaler = new ImageScaler(GraphicsUtils.getConvertedIcon(MagicImages.getIcon(MagicIcon.LIBRARY_ZONE)));
    }

    private final GameLayoutInfo layoutInfo;
    private final int playerIndex;
    protected ImageScaler imageScaler;
    private Dimension previewSize;
    private final Timeline timelines[] = new Timeline[3];
    private TimelineScenario.RendezvousSequence scenario;
    protected boolean drawArrow = true;
    protected Rectangle imageBounds = new Rectangle();
    private final MagicPlayer player;
    private final MagicCardDefinition card;
    private volatile float tRot = 0f;
    private Timeline inflateTimeline;
    private volatile float inflation = 1f;
    private volatile float inflationOpacity = 0f;

    // abstract methods
    protected abstract Rectangle getStart();
    protected abstract Rectangle getEnd();

    CardAnimation(MagicPlayer aPlayer, MagicCardDefinition aCard, GameLayoutInfo layoutInfo) {
        this.player = aPlayer;
        this.card = aCard;
        this.playerIndex = aPlayer.getIndex();
        this.layoutInfo = layoutInfo;
    }

    protected MagicPlayer getPlayer() {
        return player;
    }

    protected MagicCardDefinition getCard() {
        return card;
    }

    private boolean isPreviewTimelineRunning() {
        return (timelines[1] != null && timelines[1].getState() == Timeline.TimelineState.PLAYING_FORWARD);
    }

//    private void drawOriginHighlight(final Graphics g) {
//        final Graphics2D g2d = (Graphics2D) g.create();
//        final Rectangle r1 = getStart();
//        g2d.setStroke(new BasicStroke(6));
//        g2d.setColor(ThemeFactory.getInstance().getCurrentTheme().getColor(Theme.COLOR_CHOICE_BORDER));
//        g2d.drawRect(r1.x, r1.y, r1.width, r1.height);
//        g2d.dispose();
//    }

//    private void drawInflatedZoneIcon(final Graphics g) {
//        final Composite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, inflationOpacity);
//        final Graphics2D g2d = (Graphics2D) g.create();
//        final Rectangle r1 = getStart();
//        final Dimension d1 = zoneImageScaler.getOrigSize();
//        final Dimension d2 = zoneImageScaler.getScaledSize();
//        final int x = r1.x + 4;
//        final int xAdj = (d2.width - d1.width) / 2;
//        final int y = r1.y + 4;
//        final int yAdj = (d2.height - d1.height) / 2;
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2d.setComposite(composite);
//        g2d.drawImage(zoneImageScaler.getImage(), x - xAdj, y - yAdj, null);
//        g2d.dispose();
//    }

    private void drawCardShadow(Graphics g) {
        if (AnimationFx.isOn(AnimationFx.CARD_SHADOW)) {
            final int SHADOW = 6;
            g.setColor(SHADOW_COLOR);
            g.fillRect(
                imageBounds.x + SHADOW,
                imageBounds.y + SHADOW,
                imageScaler.getImage().getWidth(null),
                imageScaler.getImage().getHeight(null)
            );
        }
    }

    private void drawArrow(Graphics g) {
        if (drawArrow && AnimationFx.isOn(AnimationFx.FROM_ARROW)) {
            ArrowBuilder.drawArrow(g, getStart(), imageBounds);
        }
    }

    private boolean animateCardFlip() {
        return AnimationFx.isOn(AnimationFx.FLIP_CARD)
            && timelines[0] != null
            && timelines[0].getState() == Timeline.TimelineState.PLAYING_FORWARD;
    }

    @Override
    protected void render(Graphics g) {

        if (imageBounds.isEmpty())
            return;

//        if (drawArrow) {
//            ArrowBuilder.drawArrow(g, getStart(), imageBounds);
//        }
        
//        drawOriginHighlight(g);

//        if (inflateTimeline != null && !inflateTimeline.isDone()) {
//            drawInflatedZoneIcon(g);
//        }

        if (isPreviewTimelineRunning()) {
            drawArrow(g);
            drawCardShadow(g);
        }

        if (animateCardFlip()) {
            final int w = Math.abs((int)(Math.cos(tRot * 3.141592653589793D) * imageBounds.width));
            g.drawImage(
                tRot > 0.5f ? imageScaler.getImage() : backImageScaler.getImage(),
                imageBounds.x + ((imageBounds.width - w) / 2),
                imageBounds.y,
                w,
                imageBounds.height,
                getCanvas()
            );
        } else {
            g.drawImage(
                imageScaler.getImage(),
                imageBounds.x,
                imageBounds.y,
                getCanvas()
            );
        }
        
    }

    private Dimension getCardPreviewSize() {
        final Dimension max = CardImagesProvider.PREFERRED_CARD_SIZE;
        final Dimension container = layoutInfo.getGamePanelSize();
        if (container.height < max.height) {
            final int newWidth = (int)((container.height / (double)max.height) * max.width);
            return new Dimension(newWidth, container.height);
        } else {
            return max;
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
    
    private Timeline getZoomDownTimeline(int duration) {
        final Timeline tline = getMoveScaleTimeline(getPreviewRectangle(), getEnd(), duration);
        tline.addCallback(new TimelineCallbackAdapter() {
            @Override
            public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
                if (newState == Timeline.TimelineState.DONE) {
                    scenario.cancel();
                } else if (newState == Timeline.TimelineState.PLAYING_FORWARD) {
                    drawArrow = false;
                }
            }
        });
        return tline;
    }

    private Timeline getZoomUpTimeline(int duration) {
        return getMoveScaleTimeline(getStart(), getPreviewRectangle(), duration);
    }

    /**
     * This is called by timeline (do not delete!).
     */
    public void setImageBounds(final Rectangle bounds) {
        assert !SwingUtilities.isEventDispatchThread();
        imageBounds = bounds;
        final Rectangle r = new Rectangle(bounds);
        imageScaler.setSize(r.getSize());
        if (tRot > 0.5) {
            imageScaler.setSize(r.getSize());
        } else {
            backImageScaler.setSize(r.getSize());
        }
        getCanvas().repaint();

//        try {
//            SwingUtilities.invokeAndWait(() -> {});
//        } catch (InterruptedException | InvocationTargetException ex) {
//            throw new RuntimeException();
//        }
    }    

    private Timeline getMoveScaleTimeline(Rectangle from, Rectangle to, int duration) {
        final Timeline timeline = new Timeline(this);
        timeline.addPropertyToInterpolate("ImageBounds", from, to);
        timeline.setDuration(duration);
        timeline.setEase(new Spline(0.8f));
        return timeline;
    }

    /**
     * Amount of time in millisecs that full-size card preview is displayed.
     */
    private int getPauseDuration() {
        return card.hasType(MagicType.Land)
            ? GeneralConfig.getInstance().getLandPreviewDuration()
            : GeneralConfig.getInstance().getNonLandPreviewDuration();
    }

    private Timeline getPauseTimeline(final int duration) {
        final Timeline timeline = new Timeline();
        timeline.setDuration(duration);
        return timeline;
    }
    
    public void setHorizRotation(final float value) {
        tRot = value;
    }

    private Timeline getVrHzRotateTimeline(int duration) {
        final Timeline timeline = new Timeline(this);
        timeline.addPropertyToInterpolate("HorizRotation", 0.0f, 1.0f);
        timeline.setDuration(duration);
        return timeline;
    }

    public void setInflation(final float f) {
        this.inflation = f;
        zoneImageScaler.setSize(f);
//        getCanvas().repaint();
    }

    public void setInflationOpacity(final float f) {
        this.inflationOpacity = f > 1.0f ? 1f : f;
    }

    private Timeline getInflationTimeline(int duration) {
        final Timeline timeline = new Timeline(this);
        timeline.addPropertyToInterpolate("Inflation", 1.0f, 3.0f);
        timeline.addPropertyToInterpolate("InflationOpacity", 2.0f, 0.0f);
        timeline.setDuration(500);
        timeline.setInitialDelay(100);
        return timeline;
    }

    private void setupTimelineScenario() {

        timelines[0] = getZoomUpTimeline(ZOOM_DURATION);
        timelines[1] = getPauseTimeline(getPauseDuration());
        timelines[1].addCallback(new TimelineCallbackAdapter() {
            @Override
            public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
                if (newState == Timeline.TimelineState.PLAYING_FORWARD) {
                    getCanvas().repaint();
                }
            }
        });
        timelines[2] = getZoomDownTimeline(ZOOM_DURATION-400);

        final Timeline rotate = getVrHzRotateTimeline(400);
        rotate.setInitialDelay(ZOOM_DURATION - 400);

        inflateTimeline = getInflationTimeline((int)(ZOOM_DURATION * 0.8f));

        scenario = new TimelineScenario.RendezvousSequence();
        
        scenario.addScenarioActor(timelines[0]);
        scenario.addScenarioActor(rotate);
        scenario.addScenarioActor(inflateTimeline);
        scenario.rendezvous();
        scenario.addScenarioActor(timelines[1]);
        scenario.rendezvous();
        scenario.addScenarioActor(timelines[2]);
        
        scenario.addCallback(() -> {
            SwingUtilities.invokeLater(() -> {
                imageBounds = new Rectangle();
                getCanvas().setVisible(false);
            });
            isRunning.set(false);
        });

    }

    @Override
    protected void play() {
        assert SwingUtilities.isEventDispatchThread();

        isRunning.set(true);

        this.imageScaler = new ImageScaler(CachedImagesProvider.getInstance().getImage(card, 0, true));
        this.previewSize = getCardPreviewSize();

        setupTimelineScenario();

        scenario.play();
    }

    @Override
    protected void cancel() {
        if (scenario != null && scenario.getState() == TimelineScenario.TimelineScenarioState.PLAYING) {
            scenario.cancel();
        }
    }

    @Override
    protected void doCancelAction() {
        if (timelines[1].getState() == Timeline.TimelineState.PLAYING_FORWARD) {
            timelines[2].play();
        }
    }

    protected int getPlayerIndex() {
        return playerIndex;
    }

    protected GameLayoutInfo getLayoutInfo() {
        return layoutInfo;
    }

    protected Rectangle getImageBounds() {
        return imageBounds;
    }

    protected Image getScaledImage() {
        return imageScaler.getImage();
    }

}
