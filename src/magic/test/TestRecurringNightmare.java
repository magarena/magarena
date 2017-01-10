package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

/*
* The creature sacrificed to activate Recurring Nightmare should not be a legal target for reanimation.
*
* Issue #573
* */

class TestRecurringNightmare extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(1);
        addToLibrary(P, "Plains", 10);
        addToGraveyard(P, "Pale Bears", 1);
        createPermanent(P,"Swamp",false,8);
        createPermanent(P,"Recurring Nightmare",false,1);
        createPermanent(P,"Grizzly Bears",false,1);

        P = opponent;

        P.setLife(1);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Swamp",false,8);

        return game;
    }
}
