package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestCunningSparkmage extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel = createDuel();
        final MagicGame game = duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player = game.getPlayer(0);
        final MagicPlayer opponent = game.getPlayer(1);

        MagicPlayer P = player;

        //
        // HUMAN
        //
        P.setLife(20);
        // Library
        addToLibrary(P, "Forest", 10);
        // Battlefield
        createPermanent(game, P, "Forest", false, 6);
        createPermanent(game, P, "Quest for the Gemblades", false, 1);
        createPermanent(game, P, "3/3 green Elephant creature token", false, 1);
        createPermanent(game, P, "1/1 green Snake creature token", false, 1);
        createPermanent(game, P, "Strangleroot Geist", false, 2);
        createPermanent(game, P, "2/2 green Wolf creature token", false, 1);
        // Hand
        addToHand(P, "Forest", 1);
        addToHand(P, "Penumbra Spider", 1);
        addToHand(P, "Drudge Beetle", 1);

        P = opponent;
        //
        // AI
        //
        P.setLife(10);
        // Library
        addToLibrary(P, "Mountain", 10);
        addToLibrary(P, "Swamp", 10);
        // Battlefield
        createPermanent(game, P, "Blood Crypt", true, 1);
        createPermanent(game, P, "Dragonskull Summit", false, 1);
        createPermanent(game, P, "Mountain", true, 1);
        createPermanent(game, P, "Rakdos Guildgate", false, 1);
        createPermanent(game, P, "Cunning Sparkmage", false, 1);
        createPermanent(game, P, "Ember Hauler", false, 1);
        createPermanent(game, P, "Lavaborn Muse", false, 1);
        // Hand
        addToHand(P, "Mountain", 2);

        return game;
    }
}
