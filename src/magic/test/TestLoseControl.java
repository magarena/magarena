package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestLoseControl extends TestGameBuilder {
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
        createPermanent(P,"Forest",5);
        createPermanent(P,"Island",4);
        addToHand(P,"Sower of Temptation",1);


        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Forest", 10);
        createPermanent(P,"Thelonite Druid");
        createPermanent(P,"Forest",1);

        return game;
    }
}
