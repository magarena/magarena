package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;
import magic.ai.MagicAIImpl;

class TestSpineToHand extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel(MagicAIImpl.MCTS, 6);
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(10);
        addToLibrary(P, "Island", 20);
        createPermanent(P, "Island", false, 8);
        createPermanent(P, "Master Transmuter", false, 1);
        createPermanent(P, "Spine of Ish Sah", false, 1);

        P = opponent;

        P.setLife(1);
        addToLibrary(P, "Island", 20);

        return game;
    }
}
