package magic.ui.screen.cardflow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import magic.ui.dialog.prefs.ImageSizePresets;
import magic.ui.helpers.ImageHelper;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.TimelineCallback;

@SuppressWarnings("serial")
class CardFlowPanel extends JPanel implements TimelineCallback {

    private static final int SLOT_OVERLAP = 140;

    private ImageSizePresets sizePreset;

    private int activeImageIndex = 0;
    private final CardFlowTimeline timeline;
    private BufferedImage cardflowImage;
    private Dimension currentSize = new Dimension();
    private float timelinePulse = 1.0f;
    private Color imageBackgroundColor = getBackground();
    private final List<Rectangle> slots = new ArrayList<>();
    private Rectangle activeSlot;
    private Dimension selectedImageSize = getMaxImageSize();
    private final List<ICardFlowListener> listeners = new ArrayList<>();
    private final ICardFlowProvider provider;
    private final ScreenSettings settings;

    private enum FlowDirection {
        LEFT,
        RIGHT
    }
    private FlowDirection flowDirection = FlowDirection.RIGHT;

    CardFlowPanel(final ICardFlowProvider provider, ScreenSettings settings) {
        this.provider = provider;
        this.settings = settings;
        sizePreset = settings.getImageSizePreset();
        setScrollUsingMouseWheel();
        setScrollKeys();
        timeline = new CardFlowTimeline(this, provider.getAnimationDuration());
        activeImageIndex = provider.getStartImageIndex();
        setOnMouseClick();
        setLayout(null);
        setOpaque(false);
        setRedrawOnResize();
    }

    private void repaintCardFlowImage() {
        currentSize = new Dimension(getSize());
        calculateImageSlots();
        createCardFlowImage();
        repaint();
    }

    private void setRedrawOnResize() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (getSize().equals(currentSize) == false) {
                    repaintCardFlowImage();
                }
            }
        });
    }

    private void createCardFlowImage() {

        cardflowImage = ImageHelper.getCompatibleBufferedImage(
            getWidth(),
            selectedImageSize.height,
            settings.useOpaqueImage() ? Transparency.OPAQUE : Transparency.TRANSLUCENT
        );

        final Graphics2D g2d = cardflowImage.createGraphics();

        if (settings.useOpaqueImage()) {
            g2d.setColor(imageBackgroundColor);
            g2d.fillRect(0, 0, cardflowImage.getWidth(), cardflowImage.getHeight());
        }

        if (getSourceSize() > 0) {
            drawCards(g2d);
        }

        g2d.dispose();
    }

    private void setOnMouseClick() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getPoint().x < activeSlot.x) {
                    doClickLeft();
                } else if (e.getPoint().x > activeSlot.x + activeSlot.width) {
                    doClickRight();
                } else {
                    notifyOnMouseClick();
                }
            }
        });
    }

    private int getSourceSize() {
        return provider.getImagesCount();
    }

    private void drawCards(final Graphics2D g2d) {

        // identify index of active slot (normally middle-most).
        final int activeSlotIndex = slots.indexOf(activeSlot);

        drawLeadingImages(g2d, activeSlotIndex);
        drawTrailingImages(g2d, activeSlotIndex);
        doAnimateSwapTopCard(g2d, activeSlotIndex);

    }

//    /**
//     * Useful for debugging.
//     */
//    private void drawSlots(final Graphics2D g2d) {
//        // draw all available visible slots.
//        g2d.setColor(Color.GRAY);
//        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[]{2, 2}, 0));
//        for (Rectangle r : slots) {
//            g2d.drawRect(r.x, r.y, r.width - 1, r.height - 1);
//        }
//    }

    private BufferedImage getImage(int index) {
        return provider.getImage(index);
    }

    private void drawLeadingImages(final Graphics2D g2d, final int activeSlotIndex) {

        if (activeImageIndex > 0) {

            final int startImage = Math.max(activeImageIndex - activeSlotIndex, 0);
            final int endImage = activeImageIndex - (flowDirection == FlowDirection.LEFT ? 1 : 0);

            for (int i = startImage; i < endImage; i++) {

                final int imageSlot = activeSlotIndex - (activeImageIndex - i);

                final Rectangle startRect = flowDirection == FlowDirection.RIGHT
                        ? imageSlot > 0
                                ? slots.get(imageSlot - 1)
                                : slots.get(imageSlot)
                        : slots.get(imageSlot + 1);

                final Rectangle endRect = slots.get(imageSlot);

                final Rectangle drawRect = new Rectangle(
                        startRect.x - (int) ((startRect.x - endRect.x) * timelinePulse),
                        0,
                        startRect.width + (int) ((endRect.width - startRect.width) * timelinePulse),
                        startRect.height + (int) ((endRect.height - startRect.height) * timelinePulse)
                );

                final BufferedImage image = getImage(i);
                g2d.drawImage(image, drawRect.x, drawRect.y, drawRect.width, drawRect.height, null);

            }
        }
    }

    private void drawTrailingImages(final Graphics2D g2d, final int activeSlotIndex) {

        if (activeImageIndex < getSourceSize()) {

            final int startImage =
                    Math.min(slots.size() - activeSlotIndex, getSourceSize() - activeImageIndex)
                    + activeImageIndex
                    - 1;

            final int endImage = activeImageIndex + (flowDirection == FlowDirection.RIGHT ? 1 : 0);

            for (int i = startImage; i > endImage; i--) {

                final int imageSlot = (activeSlotIndex - activeImageIndex) + i;

                final Rectangle startRect = flowDirection == FlowDirection.RIGHT
                        ? slots.get(imageSlot - 1)
                        : imageSlot < slots.size() - 1
                                ? slots.get(imageSlot + 1)
                                : slots.get(imageSlot);

                final Rectangle endRect = slots.get(imageSlot);

                final Rectangle drawRect = new Rectangle(
                        startRect.x - (int) ((startRect.x - endRect.x) * timelinePulse),
                        0,
                        startRect.width + (int) ((endRect.width - startRect.width) * timelinePulse),
                        startRect.height + (int) ((endRect.height - startRect.height) * timelinePulse)
                );

                final BufferedImage image = getImage(i);
                g2d.drawImage(image, drawRect.x, drawRect.y, drawRect.width, drawRect.height, null);

            }
        }
    }

    private void drawActiveImage(final Graphics2D g2d, final int activeSlotIndex) {

        final Rectangle startRect = flowDirection == FlowDirection.RIGHT
                ? slots.get(activeSlotIndex - 1)
                : slots.get(activeSlotIndex + 1);

        final Rectangle endRect = slots.get(activeSlotIndex);

        final int adjX = (int)((startRect.x - endRect.x) * timelinePulse);
        final int adjX2 = (int)(getBellShapedFunctionB(timelinePulse));

        final Rectangle imageRect = new Rectangle(
                (startRect.x - adjX) + (flowDirection == FlowDirection.LEFT ? adjX2 : -adjX2),
                0,
                startRect.width + (int) ((endRect.width - startRect.width) * timelinePulse),
                startRect.height + (int) ((endRect.height - startRect.height) * timelinePulse)
        );

        BufferedImage image = ImageHelper.scale(
            getImage(activeImageIndex), imageRect.width, imageRect.height
        );
        g2d.drawImage(image, imageRect.x, imageRect.y, null);

    }

    private void drawActiveImageMinusOne(final Graphics2D g2d, final int activeSlotIndex) {

        if (activeImageIndex == 0) {
            return;
        }

        final Rectangle startRect = slots.get(activeSlotIndex);

        final Rectangle endRect = flowDirection == FlowDirection.RIGHT
                ? slots.get(activeSlotIndex + 1)
                : slots.get(activeSlotIndex - 1);

        final int adjX = (int)((startRect.x - endRect.x) * timelinePulse);
        final int adjX2 = (int)(getBellShapedFunctionB(timelinePulse) * SLOT_OVERLAP);

        final Rectangle imageRect = new Rectangle(
                (startRect.x - adjX) - adjX2,
                0,
                startRect.width + (int) ((endRect.width - startRect.width) * timelinePulse),
                startRect.height + (int) ((endRect.height - startRect.height) * timelinePulse)
        );

        g2d.drawImage(getImage(activeImageIndex - 1),
                imageRect.x,
                imageRect.y,
                imageRect.width,
                imageRect.height,
                null);

    }

    private void drawActiveImagePlusOne(final Graphics2D g2d, final int activeSlotIndex) {

        if (activeImageIndex == getSourceSize() - 1) {
            return;
        }

        final Rectangle startRect = slots.get(activeSlotIndex);
        final Rectangle endRect = slots.get(activeSlotIndex + 1);

        final int adjX = (int)((endRect.x - startRect.x) * timelinePulse);
        final int adjX2 = (int)(getBellShapedFunctionB(timelinePulse) * SLOT_OVERLAP);

        final Rectangle imageRect = new Rectangle(
                (startRect.x + adjX) + adjX2,
                0,
                startRect.width + (int) ((endRect.width - startRect.width) * timelinePulse),
                startRect.height + (int) ((endRect.height - startRect.height) * timelinePulse)
        );

        g2d.drawImage(getImage(activeImageIndex + 1),
                imageRect.x,
                imageRect.y,
                imageRect.width,
                imageRect.height,
                null);

    }

    private void drawDeactivatedImage(final Graphics2D g2d, final int activeSlotIndex) {
        if (flowDirection == FlowDirection.LEFT) {
            drawActiveImageMinusOne(g2d, activeSlotIndex);
        } else {
            drawActiveImagePlusOne(g2d, activeSlotIndex);
        }
    }

    private void doAnimateSwapTopCard(final Graphics2D g2d, final int activeSlotIndex) {
        if (timelinePulse < 0.51f) {
            drawActiveImage(g2d, activeSlotIndex);
            drawDeactivatedImage(g2d, activeSlotIndex);
        } else {
            drawDeactivatedImage(g2d, activeSlotIndex);
            drawActiveImage(g2d, activeSlotIndex);
        }
    }

    private void calculateImageSlots() {

        slots.clear();

        // Canvas dimensions.
        final int canvasWidth = getWidth();
        final int canvasHeight = getHeight();

        final Dimension maxImageSize = getMaxImageSize();
        final double imageAspectRatio = maxImageSize.width / (double) maxImageSize.height;

        selectedImageSize = maxImageSize.height > canvasHeight
            ? new Dimension((int)(canvasHeight * imageAspectRatio), canvasHeight)
            : maxImageSize;

        int x = canvasWidth / 2 - selectedImageSize.width / 2;
        int xLeft = x;
        int xRight = x;
        double scale = 1.0;
        int w = selectedImageSize.width;
        int h = selectedImageSize.height;
        int gap = -SLOT_OVERLAP;

        activeSlot = new Rectangle(x, 0, w, h);
        slots.add(activeSlot);

        boolean exitLoop;

        do {

            xRight = xRight + w + gap;

            scale -= 0.1;
            w = (int) (selectedImageSize.width * scale);
            h = (int) (selectedImageSize.height * scale);

            final int newLeftX = xLeft - gap - w;
            exitLoop = xLeft < 0 || newLeftX >= xLeft;

            xLeft = newLeftX;

            slots.add(0, new Rectangle(xLeft, 0, w, h));
            slots.add(new Rectangle(xRight, 0, w, h));

        } while (xLeft+w >= 0 && w > 0 && !exitLoop);

//        System.out.println("Total number of slots = " + slots.size() + " (visible=" + (slots.size() - 2) + ")");

    }

    private void setScrollUsingMouseWheel() {
        addMouseWheelListener((MouseWheelEvent ev) -> {
            if (ev.getPreciseWheelRotation() > 0) { // rotate wheel towards you.
                doClickRight();
            } else if (ev.getWheelRotation() < 0) { // rotate wheel away from you.
                doClickLeft();
            }
        });
    }

    private void setScrollKeys() {
        // Right arrow key
        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "ScrollRight");
        getActionMap().put("ScrollRight", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                doClickRight();
            }
        });
        // Left arrow key
        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "ScrollLeft");
        getActionMap().put("ScrollLeft", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                doClickLeft();
            }
        });
    }

    @Override
    public void onTimelinePulse(float arg0, float arg1) {
        timelinePulse = arg1;
        createCardFlowImage();
        repaint();
    }

    @Override
    public void onTimelineStateChanged(Timeline.TimelineState arg0, Timeline.TimelineState arg1, float arg2, float arg3) {
        if (arg0 == Timeline.TimelineState.DONE && arg1 == Timeline.TimelineState.IDLE) {
            onTimelinePulse(0, 1.0f);
            notifyOnNewActiveImage();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (cardflowImage != null) {
            final Rectangle clipRect = new Rectangle(
                    0,
                    this.getHeight() / 2 - cardflowImage.getHeight() / 2,
                    cardflowImage.getWidth(),
                    cardflowImage.getHeight());
            g.setClip(clipRect);
            g.drawImage(cardflowImage, clipRect.x, clipRect.y, this);
        }
    }

    private void notifyOnMouseClick() {
        for (ICardFlowListener listener : listeners) {
            listener.cardFlowClicked();
        }
    }

    private void notifyOnNewActiveImage() {
        for (ICardFlowListener aListener : listeners) {
            aListener.setNewActiveImage(activeImageIndex);
        }
    }

    public void addListener(ICardFlowListener aListener) {
        listeners.add(aListener);
    }

    public int getImagesCount() {
        return getSourceSize();
    }

    public void doClickLeft() {
        if (timeline.getState() == Timeline.TimelineState.IDLE) {
            if (activeImageIndex > 0) {
                flowDirection = FlowDirection.RIGHT;
                activeImageIndex = activeImageIndex - 1;
                timelinePulse = 0.0f;
                timeline.play();
            }
        }
    }

    public void doClickRight() {
        if (timeline.getState() == Timeline.TimelineState.IDLE) {
            if (activeImageIndex < getSourceSize() - 1) {
                flowDirection = FlowDirection.LEFT;
                activeImageIndex = activeImageIndex + 1;
                timelinePulse = 0.0f;
                timeline.play();
            }
        }
    }

//    /**
//     * Given a x = 0.0 to 1.0, returns f(0) = 0, f(0.5) = 1, f(1.0) = 0.
//     *
//     * see https://stackoverflow.com/questions/13097005/easing-functions-for-bell-curves.
//     */
//    private double getBellShapedFunctionA(final float x) {
//        return (Math.sin(2 * Math.PI * (x - 0.25d)) + 1) / 2.0d;
//    }

    /**
     * Given a x = 0.0 to 1.0, returns f(0) = 0, f(0.5) = 1, f(1.0) = 0.
     *
     * see https://stackoverflow.com/questions/13097005/easing-functions-for-bell-curves.
     */
    private double getBellShapedFunctionB(final float x) {
        final double sigma = 1.5d;
        return (Math.pow(4, sigma) * Math.pow(x, sigma - 1) * Math.pow(1 - x, sigma - 1)) / 4d;
    }

    @Override
    public void setBackground(Color bg) {
        imageBackgroundColor = bg;
        // Ideally panel height should be at preferred size to match height of
        // card flow image but if not then delineate image and this JPanel (on
        // which the image is painted) with a darker background color.
        super.setBackground(bg.darker());
    }

    private Dimension getMaxImageSize() {
        if (sizePreset == null || sizePreset == ImageSizePresets.SIZE_ORIGINAL) {
            return ImageSizePresets.SIZE_312x445.getSize();
        } else {
            return sizePreset.getSize();
        }
    }

    void setImageSize(ImageSizePresets preset) {
        this.sizePreset = preset;
        repaintCardFlowImage();
    }

}
