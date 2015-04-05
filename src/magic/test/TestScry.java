package magic.test;

import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerDefinition;
import magic.model.phase.MagicMainPhase;

class TestScry extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(6);
        addToLibrary(P, "Forest", 2);
        addToLibrary(P, "Island", 2);
        addToLibrary(P, "Entreat the Angels", 1);
        addToLibrary(P, "Sliver Overlord", 1);
        createPermanent(game,P, "Rupture Spire", false, 10);
        addToHand(P, "Spark Jolt", 1);
        addToHand(P, "Temple of Silence", 1);

        P = opponent;

        P.setLife(6);
        addToLibrary(P, "Temple of Silence", 20);
        createPermanent(game,P,"Rupture Spire",false,1);
        createPermanent(game,P, "Grizzly Bears", false, 1);
        addToHand(P, "Spark Jolt", 1);
        addToHand(P, "Temple of Silence", 1);

        return game;
    }
}
