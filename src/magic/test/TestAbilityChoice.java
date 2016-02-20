package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestAbilityChoice extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        //Enchant, target, pair everything to Filigree Sages
        //Try to determine the cost or source before activating an ability

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Island", 10);
        createPermanent(P,"Island", 15);
        createPermanent(P, "Chromatic Lantern", true, 1);
        createPermanent(P, "Filigree Sages", true, 1);
        addToHand(P,"Galvanic Alchemist");
        addToHand(P,"Immobilizing Ink");
        addToHand(P,"Sinking Feeling");
        addToHand(P, "Touch of Vitae");
        addToHand(P, "Island", 3);

        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Island", 20);
        return game;
    }
}
