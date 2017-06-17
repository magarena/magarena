package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

// demonstrates MiniMax AI also moves equipment between creatures until all mana is spend.
// interesting is how "Strider Harness" shows the bug, but "Barbed Battlegear" does not.
class TestEquip extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Forest", 10);
        createPermanent(P,"Forest",false,8);
        createPermanent(P,"Cylian Elf",false,2);
        createPermanent(P,"Strider Harness",false,1);
        addToHand(P,"Cylian Elf",1);


        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Forest", 10);
        createPermanent(P,"Forest",false,8);
        createPermanent(P,"Cylian Elf",false,2);
        createPermanent(P,"Strider Harness",false,1);
        //createPermanent(P,"Barbed Battlegear",false,1);
        addToHand(P,"Cylian Elf",1);

        return game;
    }
}
