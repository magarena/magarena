package magic.test;

import magic.ai.MagicAIImpl;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.*;

// 1. Click on Lighting Bolt
// 2. Click on Undo
// 3. wait for 1000 log messages to appear in log
// 4. Try to continue the game normally, it will be slower than before
class TestSlowLog extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel(MagicAIImpl.MCTS, 6);
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicUpkeepPhase.getInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(1);
        addToLibrary(P, "Forest", 20);
        createPermanent(P, "Island", false, 15);
        createPermanent(P, "Chromatic Lantern", false, 1);
        createPermanent(P, "Grizzly Bears", false, 1);
        addToHand(P, "Lightning Bolt", 15);

        P = opponent;

        P.setLife(2);
        addToLibrary(P, "Forest", 20);
        createPermanent(P, "Island", false, 7);
        createPermanent(P, "Chromatic Lantern", false, 1);
        createPermanent(P, "Grizzly Bears", false, 1);

        for (int i = 0; i < 1000; i++) {
            game.logMessage(P, "msg " + i);
        }

        return game;
    }
}
