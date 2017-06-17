package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestRecoverMR extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(4);
        P.setPoison(6);
        addToLibrary(P,"Plains",10);
        createPermanent(P,"Rupture Spire",false,8);
        addToHand(P,"Recover",1);
        addToGraveyard(P,"Murderous Redcap",1);
        addToGraveyard(P,"Ruthless Cullblade",1);


        P = opponent;

        P.setLife(1);
        P.setPoison(8);
        addToLibrary(P,"Island",10);
        createPermanent(P,"Rupture Spire",false,8);
        addToHand(P,"Stonework Puma",1);
        addToHand(P,"Llanowar Elves", 1);
        addToHand(P,"Prickly Boggart", 1);
        addToHand(P,"Veteran Armorer",1);

        return game;
    }
}
