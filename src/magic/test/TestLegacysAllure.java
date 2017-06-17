package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestLegacysAllure extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(9);
        addToLibrary(P, "Plains", 20);
        createPermanent(P,"Rupture Spire",false,8);
        createPermanent(P,"Legacy's Allure",false,1);
        addToHand(P,"Giant Growth",1);


        P = opponent;

        P.setLife(10);
        addToLibrary(P, "Plains", 20);
        createPermanent(P,"Rupture Spire",false,8);
        createPermanent(P,"Steward of Valeron",false,2);

        return game;
    }
}
