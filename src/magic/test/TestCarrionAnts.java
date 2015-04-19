package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;

import magic.model.phase.MagicMainPhase;

class TestCarrionAnts extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);


        MagicPlayer P = player;

        P.setLife(15);
        addToLibrary(P, "Plains", 10);
        createPermanent(game,P,"Hallowed Fountain",false,2);
        createPermanent(game,P,"Plains",false,2);
        createPermanent(game,P,"Angel of Despair",false,1);
        createPermanent(game,P,"Carrion Ants",false,1);
        addToHand(P,"Arcane Sanctum",1);
        addToHand(P,"Armadillo Cloak",1);
        addToHand(P,"Blightning",1);


        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(game,P,"Swamp",false,4);
        createPermanent(game,P,"Plains",false,2);
        createPermanent(game,P,"Carrion Ants",false,1);
        createPermanent(game,P,"Raging Gorilla",false,1);

        return game;

    }

}

