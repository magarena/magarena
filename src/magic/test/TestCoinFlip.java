
package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestCoinFlip extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Mountain", 10);
        createPermanent(P,"Mountain",false,20);
        createPermanent(P,"Tavern Swindler",false,1);
        addToHand(P,"Scoria Wurm",1);
        addToHand(P, "Wild Wurm", 3);
        addToHand(P, "Molten Birth", 1);


        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Mountain",false,20);
        createPermanent(P,"Tavern Swindler",false,1);
        createPermanent(P,"Creepy Doll",false,1);
        addToHand(P,"Scoria Wurm",1);
        addToHand(P, "Wild Wurm", 3);

        return game;
    }
}
