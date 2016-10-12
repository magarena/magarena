package magic.ui.screen.duel.decks;

import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import magic.cardBuilder.renderers.CardBuilder;
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
        this.duel = aDuel;
        this.screen = aScreen;
    }

    private Optional<MagicCardDefinition> findFirstProxyCard(MagicDeck aDeck) {
        return aDeck.stream()
                .filter(card -> MagicImages.isProxyImage(card.getCardDefinition()))
                .findFirst();
    }

    private Optional<MagicCardDefinition> findFirstProxyCardInDecks() {
        Optional<MagicCardDefinition> proxy = findFirstProxyCard(duel.getPlayer(0).getDeck());
        return proxy.isPresent() ? proxy : findFirstProxyCard(duel.getPlayer(1).getDeck());
    }

    private void loadCardBuilderIfRequired() {
        if (!CardBuilder.IS_LOADED) {
            Optional<MagicCardDefinition> proxy = findFirstProxyCardInDecks();
            if (proxy.isPresent()) {
                CardBuilder.getCardBuilderImage(proxy.get());
            }
        }
    }

    @Override
    protected MagicGame doInBackground() throws Exception {
        loadCardBuilderIfRequired();
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
