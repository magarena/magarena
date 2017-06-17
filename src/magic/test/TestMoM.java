package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestMoM extends TestGameBuilder {
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
        addToHand(P,"Mark of Mutiny",4);


        P = opponent;

        P.setLife(1);
        P.setPoison(8);
        addToLibrary(P,"Island",10);
        createPermanent(P,"Rupture Spire",false,8);
        addToHand(P,"Giant Spider",1);
        addToHand(P,"Birds of Paradise", 1);
        addToHand(P,"Sphere of the Suns",1);
        addToHand(P,"Spell Pierce",1);

        return game;
    }
}
