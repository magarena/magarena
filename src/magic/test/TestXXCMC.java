package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;
import magic.ai.MagicAIImpl;

class TestXXCMC extends TestGameBuilder {
    @Override
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
        addToHand(P, "Hangarback Walker", 1);
        addToHand(P, "Disdainful Stroke", 1);

        P = opponent;

        P.setLife(1);
        addToLibrary(P, "Island", 20);

        return game;
    }
}
