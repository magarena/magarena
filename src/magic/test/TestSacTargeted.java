package magic.test;

import magic.ai.MagicAIImpl;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestSacTargeted extends TestGameBuilder {
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
        createPermanent(P, "Steel Wall");
        createPermanent(P, "Prodigal Sorcerer");
        addToHand(P, "Valorous Stance", 4);
        addToHand(P, "Horobi, Death's Wail");
        addToHand(P, "Spinal Graft");
        addToHand(P, "Phantasmal Image");
        addToHand(P, "Withstand Death");
        createPermanent(P,"Forest", 10);
        createPermanent(P, "Chromatic Lantern");

        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Island", 20);
        createPermanent(P, "Island", 10);
        createPermanent(P, "Chromatic Lantern");
        addToHand(P, "Phantasmal Image");

        return game;
    }
}
