package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestGhostCouncil extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = opponent;

        P.setLife(2);
        addToLibrary(P,"Swamp",10);
        createPermanent(P,"Swamp",false,4);
        createPermanent(P,"Ghost Council of Orzhova",false,1);

        P = player;

        P.setLife(5);
        addToLibrary(P,"Mountain",10);
        createPermanent(P,"Mountain",false,4);
        addToHand(P,"Act of Treason",1);

        return game;
    }
}
