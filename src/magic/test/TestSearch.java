package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicDrawPhase;

class TestSearch extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicDrawPhase.getInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(6);

        addToLibrary(P, "Bayou", 1);
        addToLibrary(P, "Badlands", 1);
        addToLibrary(P, "Goblin King", 4);
        addToLibrary(P, "Forest", 20);
        addToLibrary(P, "Island", 20);
//        createPermanent(P,"Rupture Spire",false,6);
        createPermanent(P, "Grizzly Bears", false, 2);
        createPermanent(P, "Bow of Nylea", false, 1);
//        createPermanent(P, "Buried Ruin", false, 4);
//        createPermanent(P, "Petrified Field", false, 1);
        createPermanent(P,"Forest",false,2);
        createPermanent(P,"Plains",false,2);

        addToHand(P, "Misty Rainforest", 1);
        addToHand(P, "Buried Ruin", 1);
        addToHand(P, "Petrified Field", 1);
        addToHand(P, "Argivian Find", 1);

//        addToGraveyard(P, "Terramorphic Expanse", 8);
//        addToGraveyard(P, "Rampant Growth", 8);
//        addToGraveyard(P, "Farseek", 8);
//        addToGraveyard(P, "Verdant Catacombs", 1);
//        addToGraveyard(P, "Sylvan Scrying", 2);
        addToGraveyard(P, "Perilous Forays", 1);
//        addToGraveyard(P, "Primeval Titan", 1);
//        addToGraveyard(P,"Altar of Bone",1);
//        addToGraveyard(P, "Goblin Matron", 1);
//        addToGraveyard(P, "Grizzly Bears", 2);

        P = opponent;

        P.setLife(6);

        addToHand(P, "Misty Rainforest", 1);
        addToHand(P, "Buried Ruin", 1);

        addToLibrary(P, "Mountain", 20);
        createPermanent(P,"Rupture Spire",false,9);
        createPermanent(P, "Grizzly Bears", false, 1);
        addToGraveyard(P, "Ink-Eyes, Servant of Oni", 1);

        return game;
    }
}
