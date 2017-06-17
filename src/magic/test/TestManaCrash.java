package magic.test;

import magic.ai.MagicAIImpl;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.*;

class TestManaCrash extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel(MagicAIImpl.MCTS, 6);
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMulliganPhase.getInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Forest", 20);
        addToHand(P, "Kitchen Finks", 1);
        addToHand(P, "True Conviction", 1);
        addToHand(P, "Graypelt Refuge", 1);
        addToHand(P, "Temple Garden", 1);
        addToHand(P, "Plains", 1);

        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Forest", 20);
        addToHand(P, "Blazing Specter", 1);
        addToHand(P, "Liliana's Caress", 1);
        addToHand(P, "Sign in Blood", 1);
        addToHand(P, "Mountain", 1);

        return game;
    }
}
