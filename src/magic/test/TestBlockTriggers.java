package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestBlockTriggers extends TestGameBuilder {
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
        createPermanent(P,"Forest",8);
        createPermanent(P,"Plains",8);
        createPermanent(P,"Chromatic Lantern",true,1);
        addToHand(P, "Dazzling Beauty");
        addToHand(P, "Fog Patch");
        addToHand(P, "Curtain of Light");

        P = opponent;

        P.setLife(2);
        P.setPoison(0);
        addToLibrary(P,"Island",10);
        createPermanent(P,"Deepwood Wolverine");
        createPermanent(P,"Brushwagg");
        createPermanent(P,"Gustcloak Runner");
        createPermanent(P,"Thresher Beast");
        createPermanent(P,"Silkenfist Order");

        return game;
    }
}
