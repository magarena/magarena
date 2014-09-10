package magic.ui.duel;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JLayeredPane;
import magic.ui.duel.animation.AnimationCanvas;
import magic.ui.duel.viewer.CardViewer;
import magic.ui.widget.ZoneBackgroundLabel;

@SuppressWarnings("serial")
public class DuelLayeredPane extends JLayeredPane {

    private final ZoneBackgroundLabel backgroundLabel;
    private final DuelPanel duelPanel;
    private final AnimationCanvas animationCanvas;

    public DuelLayeredPane(final DuelPanel duelPanel, final ZoneBackgroundLabel backgroundLabel) {

        this.backgroundLabel = backgroundLabel;
        this.duelPanel = duelPanel;

        // BOTTOM LAYER : Background image
        add(backgroundLabel);
        setLayer(backgroundLabel, 0);
        backgroundLabel.setLocation(0, 0);

        // LAYER : duel battlefield and HUD.
        add(duelPanel);
        setLayer(duelPanel, 1);
        duelPanel.setLocation(0, 0);

        // LAYER : animation canvas
        animationCanvas = duelPanel.getAnimationCanvas();
        add(animationCanvas);
        setLayer(animationCanvas, 2);
        animationCanvas.setLocation(0, 0);
        animationCanvas.setVisible(false);        

        // TOP LAYER : card popup
        final CardViewer cardViewer = duelPanel.getImageCardViewer();
        add(cardViewer);
        setLayer(cardViewer, 3);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent event) {
                resizeComponents();
            }
        });
    }

    private void resizeComponents() {
        final Dimension size = getSize();
        backgroundLabel.setSize(size);
        duelPanel.setSize(size);
        animationCanvas.setSize(size);
        duelPanel.resizeComponents();
    }
}
