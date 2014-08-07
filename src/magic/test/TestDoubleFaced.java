package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerDefinition;
import magic.model.MagicDeckProfile;
import magic.model.phase.MagicMainPhase;

class TestDoubleFaced extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=new MagicDuel();
        duel.setDifficulty(6);

        final MagicDeckProfile profile=new MagicDeckProfile("bgruw");
        final MagicPlayerDefinition player1=new MagicPlayerDefinition("Player",false,profile,15);
        final MagicPlayerDefinition player2=new MagicPlayerDefinition("Computer",true,profile,14);
        duel.setPlayers(new MagicPlayerDefinition[]{player1,player2});
        duel.setStartPlayer(0);

        final MagicGame game=duel.nextGame(true);
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(5);
        addToLibrary(P, "Forest", 20);
        createPermanent(game,P, "Island", false, 8);
        createPermanent(game,P, "Chromatic Lantern", false, 1);
        addToHand(P, "Thraben Sentry", 2);
        addToHand(P, "Lightning Bolt", 1);
        addToHand(P, "Ludevic's Test Subject", 2);

        P = opponent;

        P.setLife(1);
        addToLibrary(P, "Forest", 20);
        createPermanent(game,P, "Island", false, 8);
        createPermanent(game,P, "Grizzly Bears", false, 1);
        addToHand(P, "Ludevic's Test Subject", 2);

        return game;
    }
}
