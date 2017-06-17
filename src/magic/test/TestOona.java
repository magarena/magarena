package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestOona extends TestGameBuilder {
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
        createPermanent(P,"Rupture Spire",false,9);
        addToHand(P, "Oona, Queen of the Fae", 1);

        P = opponent;

        P.setLife(5);
        addToLibrary(P, "Flailing Drake", 20);
        createPermanent(P,"Rupture Spire",false,9);
        addToHand(P, "Flailing Drake", 2);

        return game;
    }
}
