package magic.ui.widget.deck.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import magic.data.stats.MagicStats;
import magic.model.MagicDeck;

public class PwlWorker extends SwingWorker<String, Void> {

    private static final Logger LOGGER = Logger.getLogger(PwlWorker.class.getName());

    private final MagicDeck deckCopy;
    private final List<IPwlWorkerListener> listeners = new ArrayList<>();

    public PwlWorker(MagicDeck deck) {
        deckCopy = new MagicDeck(deck);
    }

    public void setListeners(IPwlWorkerListener... newListeners) {
        listeners.clear();
        listeners.addAll(Arrays.asList(newListeners));
    }

    @Override
    protected String doInBackground() throws Exception {
        return MagicStats.getPlayedWonLost(deckCopy);
    }

    @Override
    protected void done() {
        try {
            final String pwl = get();
            for (IPwlWorkerListener listener : listeners) {
                SwingUtilities.invokeLater(() -> listener.setPlayedWonLost(pwl));
            }
        } catch (CancellationException ex) {
            LOGGER.log(Level.INFO, "pwlWorker cancelled.");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
}
