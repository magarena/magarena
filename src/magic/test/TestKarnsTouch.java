package magic.test;

import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerDefinition;
import magic.model.phase.MagicMainPhase;

class TestKarnsTouch extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(game,P,"Forest",false,8);
        createPermanent(game,P,"Chromatic Lantern",false,1);
        createPermanent(game,P,"Phyrexian Crusader",false,3);
        addToHand(P, "Batterskull", 1);
        addToHand(P, "Flayer Husk", 1);
        addToHand(P, "Karn's Touch", 1);
        addToHand(P, "Master of Etherium", 1);

        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(game,P,"Rupture Spire",false,8);
        createPermanent(game,P,"Phyrexian Crusader",false,3);

        return game;
    }
}
