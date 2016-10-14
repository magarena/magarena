package magic.ui.screen;

import java.util.List;
import javax.swing.SwingWorker;
import magic.cardBuilder.renderers.CardBuilder;
import magic.model.MagicCardDefinition;
import magic.utility.MagicSystem;

public class ScreenLoaderWorker extends SwingWorker<Void, String> {

    private final Runnable runnable;
    private final boolean needsCardBuilder;
    private final boolean needsCardData;

    private final ScreenLoadingPanel statusPanel;

    ScreenLoaderWorker(ScreenLoadingPanel p) {
        this.statusPanel = p;
        this.runnable = p.getRunnable();
        this.needsCardBuilder = p.isCardBuilderNeeded();
        this.needsCardData = p.isCardDataNeeded();
    }

    @Override
    protected Void doInBackground() throws Exception {

        if (needsCardData) {
            publish("loading card data");
            MagicSystem.loadCards.get();
        }

        if (needsCardBuilder) {
            publish("loading proxy image generator");
            // Force CB to initialize.
            CardBuilder.getCardBuilderImage(MagicCardDefinition.UNKNOWN);
        }

        return null;
    }

    @Override
    protected void process(List<String> chunks) {
        statusPanel.setMessage(chunks.get(0));
    }

    @Override
    protected void done() {
        if (!isCancelled()) {
            runnable.run();
        } else {
            System.out.println("Screen loading cancelled.");
        }
    }

}
