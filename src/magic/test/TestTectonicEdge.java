package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestTectonicEdge extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(4);
        P.setPoison(6);
        addToLibrary(P,"Plains",10);
        createPermanent(P,"Rupture Spire",false,1);
        addToHand(P,"Tectonic Edge",1);
        addToHand(P,"Vivid Crag",1);
        addToHand(P,"Stonework Puma",1);
        addToHand(P,"Llanowar Elves",1);


        P = opponent;

        P.setLife(1);
        P.setPoison(8);
        addToLibrary(P,"Island",10);
        createPermanent(P,"Rupture Spire",false,3);
        createPermanent(P,"Tectonic Edge",false,3);
        addToHand(P,"Vines of Vastwood",1);
        addToHand(P,"Inkwell Leviathan",1);

        return game;
    }
}
