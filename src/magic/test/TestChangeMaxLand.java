
package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestChangeMaxLand extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);
        addToHand(P, "Plains", 10);
        addToHand(P, "Exploration", 1);
        addToHand(P, "Explore", 1);
        addToHand(P, "Rites of Flourishing", 1);

        P = opponent;

        P.setLife(1);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);

        return game;
    }
}
