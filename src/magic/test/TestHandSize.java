package magic.test;

import magic.ai.MagicAIImpl;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicUpkeepPhase;

class TestHandSize extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel(MagicAIImpl.MCTS,8);
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicUpkeepPhase.getInstance());
        game.setTurnPlayer(game.getPlayer(1));
        game.setTurn(24);
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Island", 22);
        createPermanent(P,"Island",5);
        addToHand(P, "Thought Nibbler", 1);
        addToHand(P, "Thought Eater", 1);
        addToHand(P, "Thought Devourer", 1);
        addToHand(P, "Island", 3);

        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Island", 20);

        return game;
    }
}
