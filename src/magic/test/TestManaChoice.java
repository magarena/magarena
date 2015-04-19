package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestManaChoice extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(game,P,"Abandoned Outpost",false,1);
        //createPermanent(game,P,"Bog Wreckage",false,1);
        createPermanent(game,P,"Ravaged Highlands",false,1);
        createPermanent(game,P,"Seafloor Debris",false,1);
        createPermanent(game,P,"Timberland Ruins",false,1);
        addToHand(P, "Gemstone Mine", 1);
        addToHand(P, "Fusion Elemental", 1);

        P = opponent;

        P.setLife(1);
        addToLibrary(P, "Plains", 10);
        createPermanent(game,P,"Rupture Spire",false,8);

        return game;
    }
}
