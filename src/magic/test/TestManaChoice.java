package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestManaChoice extends TestGameBuilder {
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
        createPermanent(P,"Abandoned Outpost",false,1);
        //createPermanent(P,"Bog Wreckage",false,1);
        createPermanent(P,"Ravaged Highlands",false,1);
        createPermanent(P,"Seafloor Debris",false,1);
        createPermanent(P,"Timberland Ruins",false,1);
        addToHand(P, "Gemstone Mine", 1);
        addToHand(P, "Fusion Elemental", 1);

        P = opponent;

        P.setLife(1);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);

        return game;
    }
}
