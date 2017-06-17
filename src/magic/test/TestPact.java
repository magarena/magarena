package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestPact extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Plains", 5);
        addToLibrary(P, "Grizzly Bears");
        addToLibrary(P, "Plains", 5);
        createPermanent(P, "Mountain", 5);
        createPermanent(P, "Island", 2);
        createPermanent(P, "Chromatic Lantern");
        addToHand(P, "Pact of the Titan");
        addToHand(P, "Pact of Negation");
        addToHand(P, "Slaughter Pact");
        addToHand(P, "Summoner's Pact");

        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);

        return game;
    }
}
