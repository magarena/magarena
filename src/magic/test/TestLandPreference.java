package magic.test;

import magic.ai.MagicAIImpl;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicUpkeepPhase;

/* There's 4 elves and a bunch of island in the AI's hand - after 4 draws it
* will draw a Breaching Leviathan which it won't be able to cast as it hasn't
* been playing any islands
*
* Attempt to display AI declining to play lands
*
* Issue #333
* */

class TestLandPreference extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel(MagicAIImpl.MCTS,8);
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicUpkeepPhase.getInstance());
        game.setTurnPlayer(game.getPlayer(1));
        game.setTurn(24);
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Forest", 22);
        createPermanent(P,"Steel Wall",4);

        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Breaching Leviathan", 1);
        addToLibrary(P, "Island", 5);
        addToHand(P, "Savaen Elves",4);
        addToHand(P, "Island", 3);
        createPermanent(P,"Forest");

        return game;
    }
}
