package magic.test;

import magic.model.MagicCounterType;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;
/*
* Issue #1066 - Solem Recruit should trigger at end of turn after activating Aethergeode Miner's ability
*/

public class TestRevolt extends TestGameBuilder {
    @Override
    protected MagicGame getGame() {
        final MagicDuel duel = createDuel();
        final MagicGame game = duel.nextGame();
        game.setPhase(MagicMainPhase.getSecondInstance());
        final MagicPlayer player = game.getPlayer(0);
        final MagicPlayer opponent = game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        P.changeCounters(MagicCounterType.Energy, 4);
        addToLibrary(P,"Forest", 20);
        createPermanent(P, "Forest", 10);
        createPermanent(P, "Chromatic Lantern");
        createPermanent(P, "Ruin Ghost");
        createPermanent(P, "Eldrazi Displacer");
        createPermanent(P,"Aethergeode Miner");
        createPermanent(P, "Solemn Recruit");
        addToHand(P,"Solemn Recruit");

        P = opponent;
        P.setLife(20);
        addToLibrary(P, "Forest", 20);

        return game;
    }
}
