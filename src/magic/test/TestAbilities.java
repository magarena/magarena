package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestAbilities extends TestGameBuilder {
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
        createPermanent(P,"Rupture Spire",false,20);
        createPermanent(P,"Accorder Paladin",false,100);
        createPermanent(P,"Lightning Greaves",false,1);
        createPermanent(P,"Sword of Body and Mind",false,1);
        createPermanent(P,"Darksteel Axe",false,1);
        createPermanent(P,"Akroma's Memorial",false,1);
        createPermanent(P,"Imperious Perfect",false,1);
        createPermanent(P,"Imperious Perfect",false,1);
        addToHand(P,"Naturalize",5);
        addToHand(P,"Demystify",3);
        addToHand(P,"Doom Blade",5);
        addToHand(P,"Boar Umbra",1);
        addToHand(P,"Brink of Disaster",1);
        addToHand(P,"Pacifism",1);
        addToHand(P,"Lightning Bolt",1);


        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);
        createPermanent(P,"Sword of Body and Mind",false,1);
        createPermanent(P,"Bladed Pinions",false,1);
        createPermanent(P,"Acidic Slime",false,3);
        addToHand(P,"Unquestioned Authority",1);

        return game;
    }
}
