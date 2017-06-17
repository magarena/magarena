package magic.test;

import magic.ai.MagicAIImpl;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicUpkeepPhase;

class TestBloodbond extends TestGameBuilder {
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
        addToLibrary(P, "Forest", 22);
        addToGraveyard(P, "Elvish Eulogist", 3);
        createPermanent(P, "Bloodbond March");
        addToHand(P, "Elvish Eulogist", 1);

        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Forest", 22);
        addToHand(P, "Elvish Eulogist",1);
        createPermanent(P,"Forest");

        return game;
    }
}
