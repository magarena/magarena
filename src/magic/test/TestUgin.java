package magic.test;

import magic.ai.MagicAIImpl;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestUgin extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel(MagicAIImpl.MCTS,8);
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Forest", 10);
        addToLibrary(P, "Steel Wall");
        addToLibrary(P, "Reaver Drone");
        addToLibrary(P, "Eldrazi Devastator");
        addToLibrary(P, "Forest", 10);
        createPermanent(P, "Forest", 10);
        createPermanent(P, "Eye of Ugin");

        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Island", 20);

        return game;
    }
}
