package magic.test;

import magic.ai.MagicAIImpl;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicUpkeepPhase;

class TestTarmogoyf extends TestGameBuilder {
    //Tarmogoyf should survive the lightning bolt
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
        addToLibrary(P, "Mountain", 22);
        createPermanent(P, "Mountain", 5);
        createPermanent(P, "Plains", 5);
        createPermanent(P, "Mind Stone", 1);
        addToHand(P, "Lightning Bolt");
        addToGraveyard(P, "Mountain");


        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Island", 5);
        createPermanent(P,"Forest", 5);
        createPermanent(P, "Tarmogoyf");
        addToGraveyard(P, "Tarmogoyf");

        return game;
    }
}
