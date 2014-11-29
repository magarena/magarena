package magic.test;

import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerDefinition;
import magic.model.phase.MagicMainPhase;

class TestHexproof extends TestGameBuilder {
    /**
     * Raging Ravine changed into 3/3 RG creature cannot block Guardian of the
     * Guildpack which has protection from monocolored
     * Fixed by making the protection check use getColorFlags in addition to getColoredTypeg
     */
    public MagicGame getGame() {
        final MagicDuel duel=new MagicDuel();
        duel.setDifficulty(6);

        final MagicDeckProfile profile=new MagicDeckProfile("bgruw");
        final MagicPlayerDefinition player1=new MagicPlayerDefinition("Player",false,profile,15);
        final MagicPlayerDefinition player2=new MagicPlayerDefinition("Computer",true,profile,14);
        duel.setPlayers(new MagicPlayerDefinition[]{player1,player2});
        duel.setStartPlayer(0);

        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        player.setLife(1);
        addToLibrary(player,"Plains",10);
        createPermanent(game,player,"Rupture Spire",false,8);
        createPermanent(game,player,"Thrun, the Last Troll",false,1);
        createPermanent(game,player,"Silhana Ledgewalker",false,1);
        createPermanent(game,player,"Troll Ascetic",false,1);
        createPermanent(game,player,"Sacred Wolf",false,1);
        addToHand(player,"Lightning Bolt",3);

        opponent.setLife(1);
        addToLibrary(opponent,"Island",10);
        createPermanent(game,opponent,"Rupture Spire",false,8);
        addToHand(opponent,"Lightning Bolt",3);

        return game;
    }
}
