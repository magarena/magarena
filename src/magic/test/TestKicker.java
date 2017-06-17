
package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestKicker extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(1);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);
        createPermanent(P,"Thieving Magpie",false,1);
        addToHand(P,"Ravaging Riftwurm",1);
        addToHand(P,"Sphinx of Lost Truths",1);
        addToHand(P,"Wolfbriar Elemental",1);
        addToHand(P,"Pincer Spider",1);
        addToHand(P,"Pouncing Kavu",1);
        addToHand(P,"Pouncing Wurm",1);
        addToHand(P,"Gatekeeper of Malakir",1);
        addToHand(P,"Lightkeeper of Emeria",1);
        addToHand(P,"Bloodhusk Ritualist",1);
        addToHand(P,"Deathforge Shaman",1);
        addToHand(P,"Into the Roil",1);

        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);
        createPermanent(P,"Thieving Magpie",false,1);
        addToHand(P, "Plains", 7);

        return game;
    }
}
