package magic.test;

import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerDefinition;
import magic.model.phase.MagicMainPhase;

class TestEvolve extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=new MagicDuel();

        final MagicDeckProfile profile=new MagicDeckProfile("bgruw");
        final MagicPlayerDefinition player1=new MagicPlayerDefinition("Player",false,profile);
        final MagicPlayerDefinition player2=new MagicPlayerDefinition("Computer",true,profile);
        duel.setPlayers(new MagicPlayerDefinition[]{player1,player2});
        duel.setStartPlayer(0);

        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(game,P,"Rupture Spire",false,8);
        addToHand(P, "Crocanura", 1);
        addToHand(P, "Clinging Anemones", 1);
        addToHand(P, "Elusive Krasis", 1);
        addToHand(P, "Experiment One", 1);
        addToHand(P, "Dragon Fodder", 1);
        addToHand(P, "Giant Growth", 1);

        P = opponent;

        P.setLife(1);
        addToLibrary(P, "Plains", 10);
        createPermanent(game,P,"Rupture Spire",false,8);
        addToHand(P, "Shambleshark", 1);
        addToHand(P, "Adaptive Snapjaw", 1);
        addToHand(P, "Cloudfin Raptor", 1);

        return game;
    }
}
