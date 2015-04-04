package magic.test;

import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerDefinition;
import magic.model.phase.MagicMainPhase;

class TestNPH extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=new MagicDuel();

        final MagicDeckProfile profile=new MagicDeckProfile("bgruw");
        final MagicPlayerDefinition player1=new MagicPlayerDefinition("Player",false,profile);
        final MagicPlayerDefinition player2=new MagicPlayerDefinition("Computer",true,profile);
        duel.setPlayers(new MagicPlayerDefinition[]{player1,player2});
        duel.setStartPlayer(0);

        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);
        opponent.setLife(1);

        addToLibrary(player,"Plains",10);
        addToLibrary(opponent,"Island",10);
        addToGraveyard(player,"Mogg Fanatic",2);
        addToGraveyard(opponent,"Island",2);
        addToHand(opponent,"Zephyr Sprite",3);
        addToHand(player,"Raging Goblin",1);
        addToHand(player,"Alloy Myr",1);
        addToHand(player,"Batterskull",1);
        addToHand(player,"Breath of Darigaaz",1);
        addToHand(player,"Cudgel Troll",1);
        addToHand(player,"Hovermyr",1);
        addToHand(player,"Shrine of Burning Rage",1);
        addToHand(player,"Unquestioned Authority",1);
        addToHand(player,"Psychic Barrier",1);
        addToHand(player,"Sheoldred, Whispering One",1);
        addToHand(player,"Sword of War and Peace",1);
        addToHand(player,"Urabrask the Hidden",1);
        addToHand(player,"Sickleslicer",1);

        createPermanent(game,player,"Raging Goblin",false,1);
        createPermanent(game,player,"Bonesplitter",false,1);
        createPermanent(game,player,"Rupture Spire",false,7);
        createPermanent(game,opponent,"Bloodrock Cyclops",false,1);
        createPermanent(game,opponent,"Silver Knight",false,1);

        return game;
    }
}
