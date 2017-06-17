package magic.test;

import magic.ai.MagicAIImpl;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicUpkeepPhase;

/*
* Recreation of game state to show how the AI keeps shuffling Rishadan Pawnshop back into the deck
*
* Issue #201
* */

class TestRishadan extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel(MagicAIImpl.MCTS,8);
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicUpkeepPhase.getInstance());
        game.setTurnPlayer(game.getPlayer(1));
        game.setTurn(24);
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(15);
        addToLibrary(P, "Forest", 22);
        createPermanent(P,"Forest",false,4);
        createPermanent(P,"Swamp",false,4);
        createPermanent(P,"Skyshroud Sentinel",false,1);
        createPermanent(P,"Vampire Hounds",true,1);
        createPermanent(P,"Soldevi Digger",false,1);
        addToHand(P,"Howling Wolf",3);
        addToHand(P,"Skyshroud Sentinel",2);

        addToGraveyard(P,"Skyshroud Sentinel",1);
        addToGraveyard(P,"Howling Wolf",1);
        addToGraveyard(P,"Golgari Cluestone",1);
        addToGraveyard(P,"Vampire Hounds",1);
        addToGraveyard(P,"Howling Wolf",1);
        addToGraveyard(P,"Skyshroud Sentinel",1);
        addToGraveyard(P,"Howling Wolf",1);
        addToGraveyard(P,"Vampire Hounds",1);

        P = opponent;

        P.setLife(8);
        addToLibrary(P, "Forest", 19);
        addToHand(P, "Vampire Hounds",1);
        createPermanent(P,"Forest",false,1);
        createPermanent(P,"Bayou",false,2);
        createPermanent(P,"Swamp",false,4);
        createPermanent(P,"Vampire Hounds",false,2);
        createPermanent(P,"Skyshroud Sentinel",false,2);
        createPermanent(P,"Howling Wolf",false,1);
        createPermanent(P,"Rishadan Pawnshop",false,1);
        addToHand(P,"Exhume",1);
        addToGraveyard(P, "Exhume", 2);

        return game;
    }
}
