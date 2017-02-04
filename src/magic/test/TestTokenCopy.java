package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;

/**
 * Displays ways of generating a token copy of a permanent -
 * Issue #1040 regarding Seance, and #1044 regarding their display with cardbuilder
 * Issue #1063 Copies of Copy Artifact lose the Enchantment type
 */

class TestTokenCopy extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(6);

        addToLibrary(P, "Forest", 20);
        addToGraveyard(P,"Captain of the Watch");
        addToHand(P, "Cackling Counterpart");
        addToHand(P, "Copy Artifact");
        addToHand(P, "Copy Enchantment");
        addToHand(P, "Tranquility");
        createPermanent(P, "Forest", 10);
        createPermanent(P, "Chromatic Lantern");
        createPermanent(P, "Cogwork Assembler");
        createPermanent(P, "Séance");
        createPermanent(P, "Myr Propagator");

        P = opponent;

        P.setLife(6);
        addToLibrary(P, "Mountain", 20);

        return game;
    }
}
