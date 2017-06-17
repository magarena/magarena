package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

// shows the AI can cast Twincast on a Twincast, creating an endless loop.
// repro:
// 1. cast Lightning Bolt, targeting the AI's Diregraf Ghoul
// 2. cast Twincast, targeting the Lightning Bolt effect on the stack
// 3. pass priority
class TestTwincastEndlessLoop extends TestGameBuilder {

    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(6);
        addToLibrary(P, "Plains", 15);
        createPermanent(P,"Rupture Spire",false,6);
        createPermanent(P,"Wall of Diffusion",false,1);
        createPermanent(P,"Eager Cadet",false,1);
        addToHand(P,"Twincast",1);
        addToHand(P,"Benalish Lancer",1);
        addToHand(P,"Accorder Paladin",1);
        addToHand(P,"Eager Cadet",1);
        addToHand(P,"Lightning Bolt",2);
        addToHand(P,"Swords to Plowshares",1);


        P = opponent;

        P.setLife(12);
        addToLibrary(P, "Swamp", 15);
        createPermanent(P,"Rupture Spire",false,4);
        createPermanent(P,"Eager Cadet",false,1);
        createPermanent(P,"Diregraf Ghoul",false,1);
        addToHand(P,"Twincast",1);

        return game;
    }
}
