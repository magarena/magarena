package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestReaperKing extends TestGameBuilder {
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
        createPermanent(P,"Plateau",false,2);
        createPermanent(P,"Tundra",false,2);
        createPermanent(P,"Savannah",false,2);
        createPermanent(P,"Scrubland",false,2);
        createPermanent(P,"Bayou",false,2);
        createPermanent(P,"Taiga",false,2);
        createPermanent(P,"Tropical Island",false,2);
        createPermanent(P,"Underground Sea",false,2);
        createPermanent(P,"Volcanic Island",false,2);
        createPermanent(P,"Badlands",false,2);
        addToHand(P,"Reaper King",4);

        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);

        return game;
    }
}
