package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestUnleash extends TestGameBuilder {
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
        addToHand(P, "Bloodfray Giant", 1);
        addToHand(P, "Carnival Hellsteed", 1);
        addToHand(P, "Chaos Imps", 1);
        addToHand(P, "Dead Reveler", 1);
        addToHand(P, "Gore-House Chainwalker", 1);
        addToHand(P, "Grim Roustabout", 1);
        addToHand(P, "Hellhole Flailer", 1);
        addToHand(P, "Rakdos Cackler", 1);
        addToHand(P, "Spawn of Rix Maadi", 1);
        addToHand(P, "Splatter Thug", 1);
        addToHand(P, "Thrill-Kill Assassin", 1);

        P = opponent;

        P.setLife(10);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);
        addToHand(P, "Bloodfray Giant", 1);
        addToHand(P, "Carnival Hellsteed", 1);

        return game;
    }
}
