package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestCipher extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(6);
        addToLibrary(P, "Swamp", 20);
        createPermanent(P,"Rupture Spire",false,9);
        createPermanent(P, "Battering Krasis", false, 1);
        addToHand(P, "Sunspire Griffin", 1);
        addToHand(P, "Hands of Binding", 1);
        addToHand(P, "Stolen Identity", 1);
        addToHand(P, "Lightning Bolt", 1);

        P = opponent;

        P.setLife(6);
        addToLibrary(P, "Mountain", 20);
        createPermanent(P,"Rupture Spire",false,9);
        createPermanent(P, "Grizzly Bears", false, 1);
        addToGraveyard(P, "Ink-Eyes, Servant of Oni", 1);

        return game;
    }
}
