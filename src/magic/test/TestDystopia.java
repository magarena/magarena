package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestDystopia extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(13);
        addToLibrary(P, "Forest", 20);
        createPermanent(P,"Dystopia");
        createPermanent(P,"Forest",10);
        addToHand(P, "Life and Limb", 1);

        P = opponent;

        P.setLife(13);
        addToLibrary(P, "Forest", 20);
        createPermanent(P,"Forest",10);
        return game;
    }
}
