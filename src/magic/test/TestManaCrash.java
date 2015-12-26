
package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicDeckProfile;
import magic.model.phase.*;
import magic.ai.MagicAIImpl;

class TestManaCrash extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel(MagicAIImpl.MMABC, 6);
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicUpkeepPhase.getInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Forest", 20);
        addToHand(P, "Forest", 1);
        addToHand(P, "Axebane Stag", 1);
        addToHand(P, "Forest", 1);
        addToHand(P, "Brushstrider", 1);
        addToHand(P, "Yavimaya Scion", 1);
        addToHand(P, "Temple of Plenty", 1);
        addToHand(P, "Reaping the Rewards", 1);
        
        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Forest", 20);
        addToHand(P, "Mountain", 1);
        addToHand(P, "Active Volcano", 1);
        addToHand(P, "Elvish Handservant", 1);
        addToHand(P, "Jayemdae Tome", 1);
        addToHand(P, "Savage Lands", 1);
        addToHand(P, "Forest", 1);
        addToHand(P, "Rupture Spire", 1);
        
        return game;
    }
}
