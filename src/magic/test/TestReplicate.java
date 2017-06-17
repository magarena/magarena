package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestReplicate extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(10);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);
        createPermanent(P,"Thieving Magpie",false,1);
        addToHand(P,"Leap of Flame",1);
        addToHand(P,"Train of Thought",1);
        addToHand(P,"Pyromatics",1);
        addToHand(P,"Shattering Spree",1);
        addToHand(P,"Grapeshot",1);

        P = opponent;

        P.setLife(10);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);
        createPermanent(P,"Thieving Magpie",false,1);
        addToHand(P,"Leap of Flame",1);
        addToHand(P,"Train of Thought",1);
        addToHand(P,"Pyromatics",1);
        addToHand(P,"Shattering Spree",1);
        addToHand(P,"Grapeshot",1);

        return game;
    }
}
