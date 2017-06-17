package magic.test;

import magic.ai.MagicAIImpl;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicUpkeepPhase;

class TestSnow extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel(MagicAIImpl.MCTS,8);
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicUpkeepPhase.getInstance());
        game.setTurnPlayer(game.getPlayer(0));
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Forest", 5);
        addToHand(P, "Coldsteel Heart",1);
        addToHand(P, "Forest", 3);
        createPermanent(P,"Snow-Covered Forest", 4);
        createPermanent(P,"Boreal Druid", 1);
        createPermanent(P, "Boreal Centaur", 2);
        createPermanent(P, "Paradise Mantle", 1);

        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Forest", 22);
        createPermanent(P,"Forest",4);

        return game;
    }
}
