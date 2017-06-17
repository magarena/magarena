
package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestPhyrexianMana extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(6);
        addToLibrary(P, "Forest", 20);
        addToLibrary(P, "Island", 20);
        addToLibrary(P, "Entreat the Angels", 20);
        addToLibrary(P, "Sliver Overlord", 1);
        addToLibrary(P, "Wingsteed Rider", 1);
        addToGraveyard(P, "Ink-Eyes, Servant of Oni", 3);
        createPermanent(P, "Rupture Spire", false, 9);
        createPermanent(P, "Rhystic Study", false, 1);
        addToHand(P, "Rupture Spire", 1);
        addToHand(P, "Feast of Blood", 1);
        addToHand(P, "Lightning Bolt", 1);
        addToHand(P, "Mental Misstep", 1);
        addToHand(P, "Vault Skirge", 1);

        P = opponent;

        P.setLife(6);
        addToLibrary(P, "Forest", 20);
        //createPermanent(P,"Rupture Spire",false,1);
        addToHand(P, "Trained Jackal", 2);
        addToGraveyard(P, "Ink-Eyes, Servant of Oni", 1);

        return game;
    }
}
