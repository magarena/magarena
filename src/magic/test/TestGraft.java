
package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestGraft extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Island", 10);
        createPermanent(P,"Llanowar Reborn",false,2);
        createPermanent(P,"Island",false,1);
        createPermanent(P,"Forest",false,2);
        addToHand(P, "Ethereal Ambush", 1);

        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);

        return game;
    }
}
