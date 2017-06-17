package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestHexproof extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        player.setLife(1);
        addToLibrary(player,"Plains",10);
        createPermanent(player,"Mountain",false,8);
        createPermanent(player,"Thrun, the Last Troll",false,1);
        createPermanent(player,"Silhana Ledgewalker",false,1);
        createPermanent(player,"Troll Ascetic",false,1);
        createPermanent(player,"Sacred Wolf",false,1);
        addToHand(player,"Lightning Bolt",3);

        opponent.setLife(1);
        addToLibrary(opponent,"Island",10);
        createPermanent(opponent,"Mountain",false,8);
        addToHand(opponent,"Lightning Bolt",3);

        return game;
    }
}
