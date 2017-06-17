package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestEvolve extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);
        addToHand(P, "Crocanura", 1);
        addToHand(P, "Clinging Anemones", 1);
        addToHand(P, "Elusive Krasis", 1);
        addToHand(P, "Experiment One", 1);
        addToHand(P, "Dragon Fodder", 1);
        addToHand(P, "Giant Growth", 1);

        P = opponent;

        P.setLife(1);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);
        addToHand(P, "Shambleshark", 1);
        addToHand(P, "Adaptive Snapjaw", 1);
        addToHand(P, "Cloudfin Raptor", 1);

        return game;
    }
}
