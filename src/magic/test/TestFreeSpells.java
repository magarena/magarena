package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestFreeSpells extends TestGameBuilder {
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
        createPermanent(P,"Plains",false,10);
        createPermanent(P,"Chromatic Lantern");
        //createPermanent(P,"Maelstrom Archangel");
        addToHand(P,"Isochron Scepter",1);
        addToHand(P,"Spellbinder",1);
        addToHand(P,"Altar's Reap",1);
        addToHand(P,"Artillerize",1);
        addToHand(P,"Crop Rotation",1);
        addToHand(P,"Headless Skaab",1);


        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);

        return game;
    }
}
