package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestKederektParasite extends TestGameBuilder {

    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Mountain", 10);
        createPermanent(P,"Kederekt Parasite",false,4);
        addToHand(P,"Mogg Fanatic",3);
        addToHand(P,"Rakdos Guildmage",3);


        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Mountain", 10);
        createPermanent(P,"Kederekt Parasite",false,4);
        addToHand(P,"Mogg Fanatic",3);
        addToHand(P,"Rakdos Guildmage",3);

        return game;
    }
}
