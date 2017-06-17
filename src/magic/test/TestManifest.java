package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestManifest extends TestGameBuilder {
    // issue #151
    // Carrion Crow should not manifest tapped
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(10);
        addToLibrary(P, "Wolf-Skull Shaman", 1);
        addToLibrary(P, "Carrion Crow", 1);
        createPermanent(P, "Plains", false, 8);
        createPermanent(P, "Chromatic Lantern", false, 1);
        addToHand(P, "Ethereal Ambush", 1);

        P = opponent;

        P.setLife(10);
        addToLibrary(P, "Forest", 20);
        createPermanent(P, "Grizzly Bears", false, 1);

        return game;
    }
}
