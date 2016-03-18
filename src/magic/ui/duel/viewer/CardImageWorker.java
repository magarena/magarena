package magic.ui.duel.viewer;

import java.awt.Dimension;
import java.awt.Image;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import magic.model.MagicCardDefinition;


public class CardImageWorker extends SwingWorker<Image, Boolean> {

    private final CardViewer viewer;
    private final MagicCardDefinition card;
    private final Dimension viewerSize;

    public CardImageWorker(CardViewer aViewer, MagicCardDefinition aCard) {
        assert SwingUtilities.isEventDispatchThread();
        this.viewer = aViewer;
        this.card = aCard;
        this.viewerSize = CardViewer.getImageSize();
    }

    @Override
    protected Image doInBackground() throws Exception {

        // if image not returned within specified millisecs,
        // set CardImagePanel to pending state.
        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> { publish(!isCancelled() && !isDone()); }, 200, TimeUnit.MILLISECONDS);

//        Thread.currentThread().sleep(ThreadLocalRandom.current().nextInt(100, 1000));

        Image image = CardViewer.getCardImage(card, viewerSize);

        executor.shutdownNow();

        return image;
    }

    @Override
    protected void done() {
        Image image = getImage();
        if (!isCancelled()) {
            viewer.setImage(image);
        }
    }

    @Override
    protected void process(List<Boolean> chunks) {
        if (chunks.get(0)) {
//            System.out.println("taking too long to get image - setting pending state");
            viewer.setImage(null);
        }
    }

    private Image getImage() {
        try {
            return get();
        } catch (InterruptedException ex) {
            System.err.println("Interrupted.");
            return null;
        } catch (CancellationException ex) {
//            System.err.println("Cancelled.");
            return null;
        } catch (ExecutionException ex) {
            throw new RuntimeException(ex);
        }
    }

}
