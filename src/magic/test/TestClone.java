package magic.test;

import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerDefinition;
import magic.model.phase.MagicMainPhase;

class TestClone extends TestGameBuilder {
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

        P.setLife(6);
        addToLibrary(P, "Swamp", 20);
        addToGraveyard(P, "Body Double", 1);
        createPermanent(game,P,"Island",false,4);
        createPermanent(game,P,"Swamp",false,4);
        createPermanent(game,P,"Glorious Anthem",false,1);
        createPermanent(game,P,"Chromatic Lantern",false,1);
        createPermanent(game,P,"Young Wolf",false,1);
        addToHand(P, "Phantasmal Image", 1);
        addToHand(P, "Clone", 1);
        addToHand(P, "Body Double", 1);
        addToHand(P, "Evil Twin", 1);
        addToHand(P, "Progenitor Mimic", 1);
        addToHand(P, "Lightning Bolt", 3);

        P = opponent;

        P.setLife(6);
        addToLibrary(P, "Mountain", 20);
        createPermanent(game,P,"Rupture Spire",false,9);
        createPermanent(game,P, "Grizzly Bears", false, 1);
        addToGraveyard(P, "Ink-Eyes, Servant of Oni", 1);

        return game;
    }
}
