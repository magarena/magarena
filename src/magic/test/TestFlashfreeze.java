package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestFlashfreeze extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(4);
        P.setPoison(0);
        addToLibrary(P,"Plains",10);
        createPermanent(P,"Rupture Spire",false,8);
        addToHand(P,"Flashfreeze",3);


        P = opponent;

        P.setLife(4);
        P.setPoison(0);
        addToLibrary(P,"Island",10);
        createPermanent(P,"Rupture Spire",false,5);
        createPermanent(P,"Tectonic Edge",false,3);
        addToHand(P,"Llanowar Elves", 1);
        addToHand(P,"Mogg Fanatic", 1);
        addToHand(P,"Prickly Boggart", 1);
        addToHand(P,"Veteran Armorer",1);

        return game;
    }
}
