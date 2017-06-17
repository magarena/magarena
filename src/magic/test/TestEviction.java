package magic.test;

import magic.ai.MagicAIImpl;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicUpkeepPhase;

class TestEviction extends TestGameBuilder {
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
        createPermanent(P, "Steel Wall", 4);
        createPermanent(P, "Island", 4);
        createPermanent(P, "Cerulean Sphinx");
        addToHand(P, "Mark of Eviction", 4);

        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Island", 22);
        createPermanent(P,"Steel Wall", 4);
        createPermanent(P, "Island", 4);
        addToHand(P, "Mark of Eviction", 4);

        return game;
    }
}
