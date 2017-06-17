package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestBloodrush extends TestGameBuilder {
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
        createPermanent(P,"Rupture Spire",false,8);
        addToHand(P, "Ghor-Clan Rampager", 1);
        addToHand(P, "Rubblehulk", 1);
        addToHand(P, "Scorchwalker", 1);
        addToHand(P, "Skinbrand Goblin", 1);
        addToHand(P, "Zhur-Taa Swine", 1);
        addToHand(P, "Wrecking Ogre", 1);
        addToHand(P, "Skarrg Goliath", 1);

        P = opponent;

        P.setLife(10);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);
        addToHand(P, "Grizzly Bears", 1);
        addToHand(P, "Manalith", 1);

        return game;
    }
}
