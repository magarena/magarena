package magic.ui.screen.about;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import magic.ui.MagicImages;
import magic.ui.MagicSound;
import magic.ui.helpers.ImageHelper;
import net.miginfocom.swing.MigLayout;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.TimelineCallback;

@SuppressWarnings("serial")
public class AboutContentPanel extends JPanel
    implements TimelineCallback {

    private Timeline dropTimeline;
    private float imageScale = 0.0f;

    AboutContentPanel() {
        setOpaque(false);
        setLayout(new MigLayout("alignx center, aligny center"));
        doDropAnimation();
    }

    private void doDropAnimation() {
        dropTimeline = new Timeline(this);
        dropTimeline.addCallback(this);
        dropTimeline.addPropertyToInterpolate("ImageScale", 6f, 1f);
        dropTimeline.setDuration(500);
        dropTimeline.play();
    }

    public void setImageScale(float f) {
        this.imageScale = f;
        repaint();
    }

    public float getImageScale() {
        return this.imageScale;
    }

    @Override
    public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
        if (newState == Timeline.TimelineState.DONE) {
            MagicSound.BOOM.play();
        }
    }

    @Override
    public void onTimelinePulse(float f, float f1) {
        // n/a
    }

    private void drawMagarenaImage(Graphics g) {
        BufferedImage image = MagicImages.ABOUT_LOGO;
        int scaledW = (int) (image.getWidth() * imageScale);
        int scaledH = (int) (image.getHeight() * imageScale);
        BufferedImage scaled = ImageHelper.scale(image, scaledW, scaledH);
        int posX = (getWidth() - scaled.getWidth()) / 2;
        int posY = (getHeight() - scaled.getHeight()) / 2;
        g.drawImage(scaled, posX, posY, null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imageScale > 0) {
            drawMagarenaImage(g);
        }
    }
    
}
