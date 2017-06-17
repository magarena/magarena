package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestDoubleStrikeDread extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(10);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);
        createPermanent(P,"Dread",false,1);
        createPermanent(P,"Dissipation Field",false,1);
        createPermanent(P,"Sword of Body and Mind",false,1);
        createPermanent(P,"Sword of Light and Shadow",false,1);
        createPermanent(P,"Hearthfire Hobgoblin", false, 1);
        createPermanent(P,"Oracle of Nectars", false, 1);
        addToGraveyard(P,"Oracle of Nectars", 1);
        addToHand(P, "Pacifism", 2);


        P = opponent;

        P.setLife(10);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);
        createPermanent(P,"Hearthfire Hobgoblin", false, 1);
        createPermanent(P,"Dread",false,1);
        createPermanent(P,"Dissipation Field",false,1);
        addToHand(P, "Pacifism", 2);

        return game;
    }
}
