package magic.ui.screen.duel.decks;

import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.ui.MagicImages;
import magic.utility.MagicSystem;

class NewGameWorker extends SwingWorker<MagicGame, Void> {

    private final MagicDuel duel;
    private final DuelDecksScreen screen;

    NewGameWorker(final MagicDuel aDuel, final DuelDecksScreen aScreen) {
        duel = aDuel;
        screen = aScreen;
    }

    @Override
    protected MagicGame doInBackground() throws Exception {
        return duel.nextGame();
    }

    @Override
    protected void done() {
        screen.setNextGame(getNextGame());
    }

    private MagicGame getNextGame() {
        try {
            return get();
        } catch (ExecutionException ex) {
            throw new RuntimeException(ex);
        } catch (InterruptedException ex) {
            System.err.println(ex);
            return null;
        } catch (CancellationException ex) {
            System.err.println("Worker cancelled : " + MagicSystem.getHeapUtilizationStats().replace("\n", ", "));
            return null;
        }
    }

}
