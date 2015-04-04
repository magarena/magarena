package magic.headless;

import magic.ai.MagicAI;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.IGameController;

public class HeadlessGameController implements IGameController {

    private final long maxDuration;
    private final MagicGame game;
    private boolean running;
    
    /** Fully artificial test game. */
    public HeadlessGameController(final MagicGame aGame, final long duration) {
        game = aGame;
        maxDuration = duration;
    }

    @Override
    public void haltGame() {
        running = false;
    }
    
    @Override
    public void runGame() {
        final long startTime = System.currentTimeMillis();
        
        running = true;
        while (running && game.advanceToNextEventWithChoice() && System.currentTimeMillis() - startTime <= maxDuration) {
            final MagicEvent event = game.getNextEvent();
            final Object[] result = getAIChoiceResults(event);
            game.executeNextEvent(result);
        }
        
        if (game.isFinished()) {
            game.advanceDuel();
        }

        if (System.currentTimeMillis() - startTime > maxDuration) {
            System.err.println("WARNING. Max time for AI game exceeded");
        }
    }

    private Object[] getAIChoiceResults(final MagicEvent event) {
        //dynamically get the AI based on the player's index
        final MagicPlayer player = event.getPlayer();
        final MagicAI ai = player.getPlayerDefinition().getPlayerProfile().getAiType().getAI();
        return ai.findNextEventChoiceResults(game, player);
    }
}
