package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerDefinition;
import magic.model.MagicDeckProfile;
import magic.model.phase.MagicMainPhase;

class TestManifest extends TestGameBuilder {
    // issue #151
    // Carrion Crow should not manifest tapped
    public MagicGame getGame() {
        final MagicDuel duel=new MagicDuel();
        duel.setDifficulty(6);

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

        P.setLife(10);
        addToLibrary(P, "Wolf-Skull Shaman", 1);
        addToLibrary(P, "Carrion Crow", 1);
        createPermanent(game,P, "Plains", false, 8);
        createPermanent(game,P, "Chromatic Lantern", false, 1);
        addToHand(P, "Ethereal Ambush", 1);

        P = opponent;

        P.setLife(10);
        addToLibrary(P, "Forest", 20);
        createPermanent(game,P, "Grizzly Bears", false, 1);

        return game;
    }
}
