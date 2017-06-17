package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestBlockerOrder extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(4);
        P.setPoison(0);
        addToLibrary(P,"Plains",10);
        createPermanent(P,"Rupture Spire",false,8);
        createPermanent(P,"Grizzly Bears",false,1);
        createPermanent(P,"Akrasan Squire",false,1);
        createPermanent(P,"Birds of Paradise",false,1);
        addToHand(P, "Giant Growth", 3);

        P = opponent;

        P.setLife(2);
        P.setPoison(0);
        addToLibrary(P,"Island",10);
        createPermanent(P,"Rupture Spire",false,5);
        createPermanent(P,"Yavimaya Wurm",false,1);
        createPermanent(P,"Yavimaya Wurm",false,1);

        return game;
    }
}
