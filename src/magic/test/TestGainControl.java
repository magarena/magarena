package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestGainControl extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);
        createPermanent(P,"Creeping Tar Pit",false,1);
        createPermanent(P,"Raging Ravine",false,1);
        createPermanent(P,"Memnarch",false,1);
        createPermanent(P,"Grizzly Bears",false,3);
        addToHand(P,"Act of Treason",1);
        addToHand(P,"Threaten",1);
        addToHand(P,"Slave of Bolas",1);
        addToHand(P,"Traitorous Blood",1);
        addToHand(P,"Zealous Conscripts",1);
        addToHand(P,"Legacy's Allure",1);
        addToHand(P,"Custody Battle",1);
        addToHand(P,"Thalakos Deceiver",1);
        addToHand(P,"Keiga, the Tide Star",1);
        addToHand(P,"Mark of Mutiny",1);


        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);
        createPermanent(P,"Grizzly Bears",false,3);
        createPermanent(P,"Helm of Kaldra",false,1);

        return game;
    }
}
