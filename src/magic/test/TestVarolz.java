
package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestVarolz extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(10);
        addToLibrary(P, "Mountain", 20);
        addToGraveyard(P, "Vexing Devil", 1);
        createPermanent(P,"Rupture Spire",false,9);
        createPermanent(P,"Vigor",false,1);
        createPermanent(P,"Goblin King",false,1);
        addToHand(P, "Varolz, the Scar-Striped", 1);

        P = opponent;

        P.setLife(10);
        addToLibrary(P, "Mountain", 20);
        createPermanent(P,"Rupture Spire",false,9);
        createPermanent(P,"Goblin King",false,2);
        addToHand(P, "Grizzly Bears", 2);

        return game;
    }
}
