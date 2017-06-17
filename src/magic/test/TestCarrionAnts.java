package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestCarrionAnts extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);


        MagicPlayer P = player;

        P.setLife(15);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Hallowed Fountain",false,2);
        createPermanent(P,"Plains",false,2);
        createPermanent(P,"Angel of Despair",false,1);
        createPermanent(P,"Carrion Ants",false,1);
        addToHand(P,"Arcane Sanctum",1);
        addToHand(P,"Armadillo Cloak",1);
        addToHand(P,"Blightning",1);


        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Swamp",false,4);
        createPermanent(P,"Plains",false,2);
        createPermanent(P,"Carrion Ants",false,1);
        createPermanent(P,"Raging Gorilla",false,1);

        return game;

    }

}

