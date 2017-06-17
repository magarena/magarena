package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestPersistUndying extends TestGameBuilder {
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
        createPermanent(P,"Rupture Spire",false,9);
        createPermanent(P,"Antler Skulkin",false,1);
        addToHand(P, "Murderous Redcap", 1);
        addToHand(P, "Vorapede", 1);
        addToHand(P, "Wingrattle Scarecrow", 1);
        addToHand(P, "Lightning Bolt", 2);
        addToHand(P, "White Knight", 1);

        P = opponent;

        P.setLife(10);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);
        addToHand(P, "Rendclaw Trow", 1);
        addToHand(P, "Young Wolf", 1);

        return game;
    }
}
