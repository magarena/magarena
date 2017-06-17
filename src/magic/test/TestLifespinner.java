package magic.test;

import magic.ai.MagicAIImpl;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicUpkeepPhase;

class TestLifespinner extends TestGameBuilder {
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
        addToLibrary(P, "Forest", 5);
        addToLibrary(P, "Kodama of the South Tree", 1);
        addToLibrary(P, "Forest", 5);
        addToHand(P, "Lifespinner",1);
        addToHand(P, "Forest", 3);
        createPermanent(P,"Forest", 4);
        createPermanent(P,"Traproot Kami", 3);

        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Forest", 22);
        createPermanent(P,"Forest",4);

        return game;
    }
}
