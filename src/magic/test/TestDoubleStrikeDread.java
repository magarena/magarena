package magic.test;

import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerDefinition;
import magic.model.phase.MagicMainPhase;

class TestDoubleStrikeDread extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(10);
        addToLibrary(P, "Plains", 10);
        createPermanent(game,P,"Rupture Spire",false,8);
        createPermanent(game,P,"Dread",false,1);
        createPermanent(game,P,"Dissipation Field",false,1);
        createPermanent(game,P,"Sword of Body and Mind",false,1);
        createPermanent(game,P,"Sword of Light and Shadow",false,1);
        createPermanent(game,P,"Hearthfire Hobgoblin", false, 1);
        createPermanent(game,P,"Oracle of Nectars", false, 1);
        addToGraveyard(P,"Oracle of Nectars", 1);
        addToHand(P, "Pacifism", 2);


        P = opponent;

        P.setLife(10);
        addToLibrary(P, "Plains", 10);
        createPermanent(game,P,"Rupture Spire",false,8);
        createPermanent(game,P,"Hearthfire Hobgoblin", false, 1);
        createPermanent(game,P,"Dread",false,1);
        createPermanent(game,P,"Dissipation Field",false,1);
        addToHand(P, "Pacifism", 2);

        return game;
    }
}
