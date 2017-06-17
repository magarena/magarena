package magic.test;

import magic.ai.MagicAIImpl;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestLossLoop extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel(MagicAIImpl.MCTS,8);
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Swamp", 20);
        createPermanent(P, "Swamp", 10);
        createPermanent(P, "Transcendence");
        createPermanent(P, "Platinum Angel");
        addToGraveyard(P, "Swamp", 2);

        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Island", 20);
        createPermanent(P, "Island", 10);

        return game;
    }
}
