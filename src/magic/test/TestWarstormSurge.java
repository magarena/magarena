package magic.test;

import magic.ai.MagicAIImpl;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestWarstormSurge extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel(MagicAIImpl.MCTS,8);
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Forest", 20);
        createPermanent(P, "Warstorm Surge");
        createPermanent(P, "Stalking Vengeance");
        addToHand(P, "Seeker of Skybreak", 4);
        createPermanent(P,"Forest", 8);

        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Island", 20);
        createPermanent(P, "Island", 10);

        return game;
    }
}
