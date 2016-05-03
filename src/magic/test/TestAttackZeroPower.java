package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;
import magic.ai.MagicAIImpl;

class TestAttackZeroPower extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel(MagicAIImpl.MCTS, 6);
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(5);
        addToLibrary(P, "Forest", 20);
        createPermanent(P, "Hunting Drake", false, 1);

        P = opponent;

        P.setLife(3);
        addToLibrary(P, "Forest", 20);
        createPermanent(P, "Island", false, 4);
        createPermanent(P, "Signal Pest", false, 3);
        //createPermanent(P, "Seeker of Skybreak", false, 1);
        //addToHand(P, "Ainok Survivalist", 1);

        return game;
    }
}
