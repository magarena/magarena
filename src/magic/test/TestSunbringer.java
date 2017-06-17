package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestSunbringer extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(1);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Plains",10);
        createPermanent(P, "Chromatic Lantern");
        createPermanent(P, "Llanowar Elves");
        addToHand(P,"Clockwork Beetle");
        addToHand(P, "Sunbringer's Touch");
        addToHand(P, "Plains", 5);

        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);
        addToHand(P,"Thieving Magpie",3);

        return game;
    }
}
