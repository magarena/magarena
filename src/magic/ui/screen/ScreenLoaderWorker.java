package magic.ui.screen;

import java.util.List;
import javax.swing.SwingWorker;
import magic.cardBuilder.renderers.CardBuilder;
import magic.model.MagicCardDefinition;
import magic.translate.MText;
import magic.utility.MagicSystem;

class ScreenLoaderWorker extends SwingWorker<Void, String> {

    // translatable strings
    private static final String _S1 = "loading card data";
    private static final String _S2 = "loading proxy image generator";

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
            publish(MText.get(_S1));
            MagicSystem.loadCards.get();
        }

        if (needsCardBuilder) {
            publish(MText.get(_S2));
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
