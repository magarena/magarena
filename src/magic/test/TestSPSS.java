package magic.test;

import magic.model.*;
import magic.model.phase.MagicMainPhase;

class TestSPSS extends TestGameBuilder {    
    /**
     * Raging Ravine changed into 3/3 RG creature cannot block Guardian of the
     * Guildpack which has protection from monocolored
     * Fixed by making the protection check use getColorFlags in addition to getColoredTypeg
     */
    public MagicGame getGame() {
        final MagicTournament tournament=new MagicTournament();
        tournament.setDifficulty(6);
        
        final MagicPlayerProfile profile=new MagicPlayerProfile("bgruw");
        final MagicPlayerDefinition player1=new MagicPlayerDefinition("Player",false,profile,15);
        final MagicPlayerDefinition player2=new MagicPlayerDefinition("Computer",true,profile,14);
        tournament.setPlayers(new MagicPlayerDefinition[]{player1,player2});
        tournament.setStartPlayer(0);
        
        final MagicGame game=tournament.nextGame(true);
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(4);
        P.setPoison(6);
        addToLibrary(P,"Plains",10);
        createPermanent(game,P,"Rupture Spire",false,8);
        addToHand(P,"Sphere of the Suns",1);
        addToHand(P,"Vivid Crag",1);
        addToHand(P,"Spell Pierce",1);
        addToHand(P,"Veteran Armorer",1);


        P = opponent;
        
        P.setLife(1);
        P.setPoison(8);
        addToLibrary(P,"Island",10);
        createPermanent(game,P,"Rupture Spire",false,8);
        addToHand(P,"Stonework Puma",1);
        addToHand(P,"Llanowar Elves", 1);
        addToHand(P,"Prickly Boggart", 1);
        addToHand(P,"Veteran Armorer",1);
        addToHand(P,"Sphere of the Suns",1);
        addToHand(P,"Spell Pierce",1);
        
        return game;
    }
}
