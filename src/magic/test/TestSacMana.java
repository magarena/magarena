package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicUntapPhase;

class TestSacMana extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicUntapPhase.getInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);
// Casting Algae Gharial should prompt for which land to sacrifice for green

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Abandoned Outpost",1);
        createPermanent(P,"Bog Wreckage",1);
        createPermanent(P,"1/1 colorless Eldrazi Scion creature token");
        createPermanent(P,"0/1 colorless Eldrazi Spawn creature token");
        addToHand(P,"Steel Wall",1);
        addToHand(P,"Armadillo Cloak",1);
        addToHand(P,"Llanowar Elves",1);
        addToHand(P,"Aesthir Glider",1);
        addToHand(P,"Algae Gharial",1);

        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Swamp",4);
        createPermanent(P,"Plains",2);

        return game;

    }

}

