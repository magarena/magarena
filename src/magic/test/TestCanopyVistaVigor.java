package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;
import magic.ai.MagicAIImpl;

class TestCanopyVistaVigor extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel(MagicAIImpl.MCTS, 6);
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(10);
        addToLibrary(P, "Forest", 20);
        createPermanent(P, "Amulet of Vigor", false, 1);
        addToHand(P, "Canopy Vista", 1);
        addToHand(P, "Stomping Ground", 1);

        P = opponent;

        P.setLife(10);
        addToLibrary(P, "Forest", 20);

        return game;
    }
}
