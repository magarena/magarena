package magic.test;

import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerDefinition;
import magic.model.phase.MagicMainPhase;

class TestCascade extends TestGameBuilder {
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

        P.setLife(13);
        addToLibrary(P, "Lightning Bolt", 1);
        addToLibrary(P, "Fling", 1);
        addToGraveyard(P, "Lightning Bolt", 1);
        createPermanent(game,P, "Mountain", false, 8);
        createPermanent(game,P, "Chromatic Lantern", false, 1);
        addToHand(P, "Bloodbraid Elf", 1);

        P = opponent;

        P.setLife(13);
        addToLibrary(P, "Forest", 20);
        addToLibrary(P, "Grizzly Bears", 1);
        addToHand(P, "Brainbite", 1);
        createPermanent(game,P,"Forest",false,5);
        createPermanent(game,P, "Glasses of Urza", false, 1);
        addToGraveyard(P, "Grizzly Bears", 1);

        return game;
    }
}
