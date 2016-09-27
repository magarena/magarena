package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestCombatControl extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(1);
        addToLibrary(P, "Island", 10);
        createPermanent(P,"Island", 7);
        createPermanent(P, "Aether Spellbomb");
        addToHand(P,"Illusory Gains");

        P = opponent;

        P.setLife(1);
        addToLibrary(P, "Island", 20);
        createPermanent(P, "Aetherplasm");
        createPermanent(P, "Dryad Arbor");
        addToHand(P, "Grizzly Bears");
        return game;
    }
}
