package magic.ui.screen;

import java.util.List;
import javax.swing.SwingWorker;
import magic.translate.MText;
import magic.utility.MagicSystem;

class ScreenLoaderWorker extends SwingWorker<Void, String> {

    // translatable strings
    private static final String _S1 = "loading card data";

    private final Runnable runnable;
    private final boolean needsCardData;

    private final ScreenLoadingPanel statusPanel;

    ScreenLoaderWorker(ScreenLoadingPanel p) {
        this.statusPanel = p;
        this.runnable = p.getRunnable();
        this.needsCardData = p.isCardDataNeeded();
    }

    @Override
    protected Void doInBackground() throws Exception {

        if (needsCardData) {
            publish(MText.get(_S1));
            MagicSystem.loadPlayable.get();
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
