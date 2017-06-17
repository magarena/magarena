
package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestWorship extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(2);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Swamp",false,4);
        createPermanent(P,"Plains",false,2);
        createPermanent(P,"Worship",false,1);
        createPermanent(P,"Air Servant",false,1);
        addToHand(P,"Doom Blade",2);
        addToHand(P,"Demystify",2);
        addToHand(P,"Stomping Ground",1);


        P = opponent;

        P.setLife(2);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Swamp",false,4);
        createPermanent(P,"Plains",false,2);
        createPermanent(P,"Worship",false,1);
        createPermanent(P,"Alpha Myr",false,1);

        return game;
    }
}
