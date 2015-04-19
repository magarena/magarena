package magic.test;

import magic.ai.MagicAI;
import magic.ai.MagicAIImpl;
import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;

import magic.model.phase.MagicMainPhase;

class TestSanguineBond extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(10);
        addToLibrary(P, "Plains", 10);
        createPermanent(game,P,"Rupture Spire",false,8);
        createPermanent(game,P,"Sanguine Bond",false,1);
        createPermanent(game,P,"Grizzly Bears",false,1);
        addToHand(P, "Exquisite Blood", 1);

        P = opponent;

        P.setLife(10);
        addToLibrary(P, "Plains", 10);
        createPermanent(game,P,"Rupture Spire",false,8);
        createPermanent(game,P,"Sanguine Bond",false,1);
        createPermanent(game,P,"Grizzly Bears",false,2);
        addToHand(P, "Exquisite Blood", 1);

        return game;
    }
}
