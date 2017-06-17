package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestThallid extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(10);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);
        addToHand(P, "Deathspore Thallid", 1);
        addToHand(P, "Thallid Devourer", 1);
        addToHand(P, "Vitaspore Thallid", 1);

        P = opponent;

        P.setLife(10);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);
        addToHand(P, "Thallid", 1);
        addToHand(P, "Thallid Shell-Dweller", 1);
        addToHand(P, "Thallid Germinator", 1);
        addToHand(P, "Savage Thallid", 1);
        addToHand(P, "Psychotrope Thallid", 1);

        return game;
    }
}
