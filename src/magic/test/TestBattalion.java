package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestBattalion extends TestGameBuilder {
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
        addToHand(P, "Bomber Corps", 1);
        addToHand(P, "Boros Elite", 1);
        addToHand(P, "Daring Skyjek", 1);
        addToHand(P, "Firefist Striker", 1);
        addToHand(P, "Firemane Avenger", 1);
        addToHand(P, "Frontline Medic", 1);
        addToHand(P, "Legion Loyalist", 1);
        addToHand(P, "Nav Squad Commandos", 1);
        addToHand(P, "Ordruun Veteran", 1);
        addToHand(P, "Warmind Infantry", 1);
        addToHand(P, "Wojek Halberdiers", 1);

        P = opponent;

        P.setLife(10);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);
        addToHand(P, "Grizzly Bears", 1);
        addToHand(P, "Manalith", 1);

        return game;
    }
}
