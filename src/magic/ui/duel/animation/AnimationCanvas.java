package magic.ui.duel.animation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import magic.ui.theme.ThemeFactory;
import magic.ui.widget.ImagePanel;
import magic.ui.widget.ImagePanel.ScaleMode;

import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.Timeline.TimelineState;
import org.pushingpixels.trident.callback.TimelineCallback;
import org.pushingpixels.trident.ease.Spline;

@SuppressWarnings("serial")
public class AnimationCanvas extends JPanel implements TimelineCallback {

    private static final Color CLEAR_CANVAS_COLOR = new Color(0, 0, 0, 0);

    private ImagePanel imagePanel;
    private final Timer previewTimer;
    private Timeline timeline1;
    private Timeline timeline2;
    private Dimension startSize;
    private Dimension endSize;
    private Point startPosition;
    private Point endPosition;
    private Dimension previewSize;
    private Point previewPosition;
    private final AtomicBoolean isBusy = new AtomicBoolean(false);
    private boolean highlightCardPosition;
    private boolean clearCanvas = false;

    public AnimationCanvas() {

        setOpaque(false);
        setLayout(null);

        previewTimer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previewTimer.stop();
                doAnimation2();
            }
        });

        setCancelActionKeysInputMap();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1) {
                    doCancelAction();
                }
            }
        });

    }

    public void playCardAnimation(
        final BufferedImage image,
        final Dimension startSize,
        final Dimension endSize,
        final Point startPosition,
        final Point endPosition,
        final Dimension previewSize,
        final boolean highlightCardPosition
    ) {

        assert SwingUtilities.isEventDispatchThread();

        isBusy.set(true);

        this.highlightCardPosition = highlightCardPosition;
        this.startSize = startSize;
        this.endSize = endSize;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.previewSize = previewSize;
        this.previewPosition = new Point(
                (getWidth() - previewSize.width) / 2,
                (getHeight() - previewSize.height) / 2);

        imagePanel = new ImagePanel(image);
        imagePanel.setScaleMode(ScaleMode.PERFORMANCE);
        imagePanel.sizeImageToFitPanel();
        imagePanel.setSize(startSize);
        imagePanel.setLocation(startPosition);
        add(imagePanel);

        // IMPORTANT do not remove - SPACE key action will not work otherwise.
        requestFocusInWindow();

        doAnimation1();

    }

    private void doAnimation1() {

        if (timeline1 != null) {
            if (timeline1.getState() != TimelineState.IDLE || previewTimer.isRunning()) {
                return;
            }
        }

        // card size
        final Dimension startSize = this.startSize;
        final Dimension endSize = this.previewSize;

        // card location
        final Point startPoint = this.startPosition;
        final Point endPoint = this.previewPosition;

        imagePanel.setLocation(startPoint);
        imagePanel.setSize(startSize);

        // timeline
        timeline1 = new Timeline();

        timeline1.addCallback(this);
        timeline1.setDuration(500);

        timeline1.addPropertyToInterpolate(
                Timeline.property("size").on(imagePanel).from(startSize).to(endSize));
        timeline1.addPropertyToInterpolate(
                Timeline.property("location").on(imagePanel).from(startPoint).to(endPoint));
//        t.addPropertyToInterpolate(
//                Timeline.property("rotation").on(p).from(0).to(1080));

        timeline1.setEase(new Spline(0.8f));

        assert SwingUtilities.isEventDispatchThread();
        timeline1.play();

    }

//    private void doThreadSleep(final long msecs) {
//        try {
//            Thread.sleep(msecs);
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

    private void doAnimation2() {

        if (timeline2 != null) {
            if (timeline2.getState() != TimelineState.IDLE || previewTimer.isRunning()) {
                return;
            }
        }

        // card size
        final Dimension startSize = this.previewSize;
        final Dimension endSize = this.endSize;

        // card location
        final Point startPoint = this.previewPosition;
        final Point endPoint = this.endPosition;

        imagePanel.setLocation(startPoint);
        imagePanel.setSize(startSize);

        // timeline
        timeline2 = new Timeline();

        timeline2.addCallback(this);
        timeline2.setDuration(500);

        timeline2.addPropertyToInterpolate(
                Timeline.property("size").on(imagePanel).from(startSize).to(endSize));
        timeline2.addPropertyToInterpolate(
                Timeline.property("location").on(imagePanel).from(startPoint).to(endPoint));
//        t.addPropertyToInterpolate(
//                Timeline.property("rotation").on(p).from(0).to(1080));

        timeline2.setEase(new Spline(0.8f));

        assert SwingUtilities.isEventDispatchThread();
        timeline2.play();

    }

    /* (non-Javadoc)
     * @see org.pushingpixels.trident.callback.TimelineCallback#onTimelineStateChanged(org.pushingpixels.trident.Timeline.TimelineState, org.pushingpixels.trident.Timeline.TimelineState, float, float)
     */
    @Override
    public void onTimelineStateChanged(TimelineState oldState, TimelineState newState, float durationFraction, float timelinePosition) {
//        System.out.println("onTimelineStateChanged, oldState = " + oldState + ", newState = " + newState);
        if (oldState == TimelineState.PLAYING_FORWARD && newState == TimelineState.DONE) {
            if (timeline1 != null) {
                timeline1 = null;
//                System.out.println("Animation 1 complete, starting preview...");
                previewTimer.start();
            } else {
                isBusy.set(false);
            }
        } else if (oldState == TimelineState.PLAYING_FORWARD && newState == TimelineState.CANCELLED) {
            isBusy.set(false);
        }
    }

    /* (non-Javadoc)
     * @see org.pushingpixels.trident.callback.TimelineCallback#onTimelinePulse(float, float)
     */
    @Override
    public void onTimelinePulse(float durationFraction, float timelinePosition) {
        // TODO Auto-generated method stub
    }

    public AtomicBoolean isBusy() {
        return isBusy;
    }

    public void setPreviewDuration(int i) {
        previewTimer.setInitialDelay(i);
    }

    private void setCancelActionKeysInputMap() {
        getActionMap().put("CancelAction", getCancelAction());
        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "CancelAction");
        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "CancelAction");
    }

    private AbstractAction getCancelAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                doCancelAction();
            }
        };
    }

    private void doCancelAction() {
        if (timeline1 != null && timeline1.getState() == TimelineState.PLAYING_FORWARD) {
            timeline1.cancel();
        } else if (timeline2 != null && timeline2.getState() == TimelineState.PLAYING_FORWARD) {
            timeline2.cancel();
        } else if (previewTimer.isRunning()) {
//            System.out.println("previewTimer Stopped");
            previewTimer.stop();
            doAnimation2();
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (clearCanvas) {
            clearCanvas = false;
            g.setColor(CLEAR_CANVAS_COLOR);
            g.fillRect(0, 0, getWidth(), getHeight());
        } else {
            if (highlightCardPosition) {
                if (startPosition != null && startSize != null) {
                    drawTransparentOverlay(g);
                    //drawBorderHighlight(g);
                }
            }
        }
    }

    /**
     *  Draws a one pixel border of choiceColor.
     */
    private void drawBorderHighlight(Graphics g) {
        final Graphics2D g2d=(Graphics2D)g;
        g2d.setPaint(Color.RED);
        int strokeWidth = 4;
        g2d.setStroke(new BasicStroke(strokeWidth));
        strokeWidth = strokeWidth / 2;
        g2d.drawRect(
                startPosition.x + strokeWidth,
                startPosition.y + strokeWidth,
                startSize.width - strokeWidth,
                startSize.height - strokeWidth);
    }

    /**
     *  Draws a transparent overlay of choiceColor.
     */
    private void drawTransparentOverlay(final Graphics g) {
        final Color choiceColor = ThemeFactory.getInstance().getCurrentTheme().getChoiceColor();
        final Graphics2D g2d = (Graphics2D)g;
        g2d.setPaint(choiceColor);
        g2d.fillRect(
                startPosition.x - 1,
                startPosition.y - 1,
                startSize.width + 2,
                startSize.height + 2);
    }

    private void clearCanvas() {
        removeAll();
        clearCanvas = true;
        repaint();
    }

    @Override
    public void setVisible(boolean isVisible) {
        super.setVisible(isVisible);
        if (!isVisible) {
            clearCanvas();
        }
    }

}
