/**
 *
 */
package magic.ui.duel.animation;

import magic.ui.duel.animation.AnimationCanvas;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import magic.data.CardImagesProvider;
import magic.data.GeneralConfig;
import magic.data.CachedImagesProvider;
import magic.model.MagicType;
import magic.ui.GamePanel;
import magic.ui.MagicFrame;

public class GamePlayAnimator {

    private final MagicFrame frame;
    private final GamePanel gamePanel;

    public GamePlayAnimator(final MagicFrame frame, final GamePanel gamePanel) {
        this.frame = frame;
        this.gamePanel = gamePanel;
    }

    /**
     * Run animation(s) and wait until complete.
     * <p>
     * This method should be run from a non-EDT thread otherwise UI would freeze.
     */
    public void runAnimation(final PlayCardFromHandAnimation animationEvent) {
        assert !SwingUtilities.isEventDispatchThread();

        runAnimationWorkerAndWait(getAnimationWorker(animationEvent));
    }

    /**
     * Runs SwingWorker and suspends thread until worker has finished.
     */
    private void runAnimationWorkerAndWait(final SwingWorker<Void, Void> worker) {
        worker.execute();
        try {
            worker.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private SwingWorker<Void, Void> getAnimationWorker(final PlayCardFromHandAnimation animationEvent) {
        return new SwingWorker<Void, Void> () {
            @Override
            protected Void doInBackground() throws Exception {
                doPlayCardFromHand(animationEvent);
                return null;
            }
        };
    }

    private void doPlayCardFromHand(final PlayCardFromHandAnimation animation) {

        assert !SwingUtilities.isEventDispatchThread();

        if (!frame.getGlassPane().isVisible()) {

            final AnimationCanvas canvas = new AnimationCanvas();
            canvas.setSize(gamePanel.getSize());

            frame.setGlassPane(canvas);
            frame.getGlassPane().setVisible(true);

            canvas.setPreviewDuration(animation.getCard().hasType(MagicType.Land) ? 5000 : 10000);

            // get original, unscaled card image from cache.
            final CardImagesProvider imageProvider = CachedImagesProvider.getInstance();
            final BufferedImage image = imageProvider.getImage(animation.getCard(), 0, true);

            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        canvas.playCardAnimation(
                                image,
                                animation.getStartSize(),
                                animation.getEndSize(),
                                animation.getStartPoint(),
                                animation.getEndPoint(),
                                getCardPreviewSize(),
                                animation.getPlayer().getIndex() == 0);
                    }
                });
            } catch (InvocationTargetException | InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            while (canvas.isBusy().get()) {
                doThreadSleep(100);
            }

//            System.out.println("Animation finished");
            frame.getGlassPane().setVisible(false);
        }

    }

    private Dimension getCardPreviewSize() {
        final Dimension max = GeneralConfig.PREFERRED_CARD_SIZE;
        final Dimension container = gamePanel.getSize();
        if (container.height < max.height) {
            final int newWidth = (int)((container.height / (double)max.height) * max.width);
            return new Dimension(newWidth, container.height);
        } else {
            return max;
        }
    }

    private void doThreadSleep(final long msecs) {
        try {
            Thread.sleep(msecs);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
