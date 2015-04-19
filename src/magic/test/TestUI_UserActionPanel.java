package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestUI_UserActionPanel extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Forest", 10);
        addToLibrary(P, "Adder-Staff Boggart", 1);
        createPermanent(game, P, "Thousand Winds", false, 3);
        createPermanent(game, P, "Island", false, 2);
        createPermanent(game, P, "Mountain", false, 3);
        createPermanent(game, P, "Forest", false, 2);
        addToHand(P, "Backslide", 1);
        addToHand(P, "Adder-Staff Boggart", 1);
        addToHand(P, "Joraga Warcaller", 1);
        addToHand(P, "Paperfin Rascal", 1);
        addToHand(P, "Skitter of Lizards", 1);
        addToHand(P, "Sylvan Echoes", 1);

        P = opponent;

        P.setLife(1);
        addToLibrary(P, "Forest", 20);
        addToLibrary(P, "Island", 8);
//        addToLibrary(P, "Grizzly Bears", 1);
        addToHand(P, "Ludevic's Test Subject", 2);

        return game;
    }
}
