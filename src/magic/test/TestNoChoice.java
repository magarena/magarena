
package magic.test;

import magic.ai.MagicAIImpl;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicUpkeepPhase;

/*
* Shows how it's possible to start activating an ability which then becomes illegal.
*
* Activate Foul Familar's ability, paying mana from Vesper Ghoul.
*
* Issue #226
* */

class TestNoChoice extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel(MagicAIImpl.MCTS, 1);
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicUpkeepPhase.getInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(1);
        addToLibrary(P, "Forest", 20);
        createPermanent(P, "Vesper Ghoul", false, 1);
        createPermanent(P, "Foul Familiar", false, 1);
        createPermanent(P, "Dune Diviner", false, 1);
        createPermanent(P, "Hostile Desert", false, 1);

        P = opponent;

        P.setLife(1);
        addToLibrary(P, "Forest", 20);
        createPermanent(P, "Vesper Ghoul", false, 1);
        createPermanent(P, "Foul Familiar", false, 1);

        return game;
    }
}
