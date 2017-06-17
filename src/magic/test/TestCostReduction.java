package magic.test;

import magic.ai.MagicAIImpl;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.*;

class TestCostReduction extends TestGameBuilder {
    /*
     * AI is able to win if it casts Ruby Medallion and plays two Mountains, this give 8 Mountains.
     * Then AI can casts Bonefire for X = 4 and be able to pay the 9 - 1 mana required
     */
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel(MagicAIImpl.MCTS, 6);
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicUpkeepPhase.getInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(4);
        addToLibrary(P, "Mountain", 20);
        createPermanent(P, "Mountain", false, 6);
        addToHand(P, "Circle of Flame", 1);
        addToHand(P, "Jund Battlemage", 1);
        addToHand(P, "Ruby Medallion", 2);
        addToHand(P, "Ruby Leech", 2);
        addToHand(P, "Enrage", 1);
        addToHand(P, "Bonfire of the Damned", 1);
        addToHand(P, "Gut Shot", 1);
        addToHand(P, "Kobolds of Kher Keep", 1);

        P = opponent;

        P.setLife(4);
        addToLibrary(P, "Wind Drake", 2);
        addToLibrary(P, "Mountain", 2);
        createPermanent(P, "Mountain", false, 6);
        addToHand(P, "Bonfire of the Damned", 1);
        addToHand(P, "Ruby Medallion", 1);

        return game;
    }
}
