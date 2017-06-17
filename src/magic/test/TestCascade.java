package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestCascade extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(13);
        addToLibrary(P, "Fling", 1);
        addToLibrary(P, "Lightning Bolt", 1);
        addToGraveyard(P, "Lightning Bolt", 1);
        createPermanent(P, "Mountain", false, 8);
        createPermanent(P, "Chromatic Lantern", false, 1);
        addToHand(P, "Bloodbraid Elf", 1);

        P = opponent;

        P.setLife(13);
        addToLibrary(P, "Forest", 20);
        addToLibrary(P, "Grizzly Bears", 1);
        createPermanent(P,"Forest",false,5);
        addToGraveyard(P, "Grizzly Bears", 1);

        return game;
    }
}
