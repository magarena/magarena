package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestBowOfNylea extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(1);
        addToLibrary(P, "Mountain", 1);
        addToGraveyard(P, "Lightning Bolt", 1);
        addToGraveyard(P, "Mountain", 10);
        addToGraveyard(P, "Lightning Bolt", 1);
        addToGraveyard(P, "Mountain", 10);
        createPermanent(P, "Mountain", false, 8);
        createPermanent(P, "Chromatic Lantern", false, 1);
        createPermanent(P, "Bow of Nylea", false, 1);

        P = opponent;

        P.setLife(1);
        addToLibrary(P, "Mountain", 1);
        addToGraveyard(P, "Lightning Bolt", 1);
        addToGraveyard(P, "Mountain", 10);
        addToGraveyard(P, "Lightning Bolt", 1);
        addToGraveyard(P, "Mountain", 10);
        createPermanent(P, "Mountain", false, 8);
        createPermanent(P, "Chromatic Lantern", false, 1);
        createPermanent(P, "Bow of Nylea", false, 1);

        return game;
    }
}
