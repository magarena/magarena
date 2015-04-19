package magic.test;

import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;

import magic.model.phase.MagicMainPhase;

class TestReaperKing extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(game,P,"Plateau",false,2);
        createPermanent(game,P,"Tundra",false,2);
        createPermanent(game,P,"Savannah",false,2);
        createPermanent(game,P,"Scrubland",false,2);
        createPermanent(game,P,"Bayou",false,2);
        createPermanent(game,P,"Taiga",false,2);
        createPermanent(game,P,"Tropical Island",false,2);
        createPermanent(game,P,"Underground Sea",false,2);
        createPermanent(game,P,"Volcanic Island",false,2);
        createPermanent(game,P,"Badlands",false,2);
        addToHand(P,"Reaper King",4);
        
        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(game,P,"Rupture Spire",false,8);

        return game;
    }
}
