package magic.test;

import magic.ai.MagicAIImpl;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicUpkeepPhase;

class TestDeathbringer extends TestGameBuilder {
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
        addToLibrary(P, "Swamp", 22);
        createPermanent(P,"Deathbringer Liege");
        createPermanent(P, "Swamp", 5);
        createPermanent(P, "Plains", 5);
        addToHand(P, "Harsh Sustenance", 1);


        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Breaching Leviathan", 1);
        addToLibrary(P, "Island", 5);
        addToHand(P, "Savaen Elves",4);
        addToHand(P, "Island", 3);
        createPermanent(P,"Forest");
        createPermanent(P, "Steel Wall");

        return game;
    }
}
