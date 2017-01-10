
package magic.test;

import magic.ai.MagicAIImpl;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicUpkeepPhase;

/*
*Attempt to show the AI 'messing around' with Noxious Revival and Brave the Elements. Occasionally loops.
*
* Issue #174
* */

class TestNoxious extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel(MagicAIImpl.MCTS, 8);
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicUpkeepPhase.getInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(10);
        addToLibrary(P, "Forest", 20);
        createPermanent(P,"Plains");
        createPermanent(P,"Forest");
        createPermanent(P, "Grizzly Bears");


        P = opponent;

        P.setLife(10);
        addToLibrary(P, "Forest", 18);
        addToGraveyard(P,"Grizzly Bears",1);
        addToGraveyard(P,"Noxious Revival",1);
        addToGraveyard(P,"Brave the Elements",1);
        addToHand(P,"Noxious Revival",1);
        createPermanent(P, "Forest");
        createPermanent(P, "Plains");

        return game;
    }
}
