package magic.test;

import magic.model.MagicCounterType;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.ChangeCountersAction;
import magic.model.phase.MagicMainPhase;

class TestTyreal extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
    final MagicDuel duel = createDuel();
    final MagicGame game = duel.nextGame();
    game.setPhase(MagicMainPhase.getFirstInstance());
    final MagicPlayer player = game.getPlayer(0);
    final MagicPlayer opponent = game.getPlayer(1);

    MagicPlayer P = player;

    P.setLife(20);
    addToLibrary(P, "Plains", 10);
    createPermanent(P, "Rupture Spire", false, 10);
    //createPermanent(P, "Goblin Bombardment", false, 1);
    //createPermanent(P, "Jayemdae Tome", false, 1);
    //createPermanent(P, "Mad Auntie", false, 1);
    //createPermanent(P, "Ib Halfheart, Goblin Tactician", false, 1);
    //createPermanent(P, "Boggart Shenanigans", false, 1);
    //createPermanent(P, "Knucklebone Witch", false, 1);
    //createPermanent(P, "Strangleroot Geist", false, 1);
    //createPermanent(P, "Vorapede", false, 1);
    addToHand(P, "Dungeon Geists", 1);
    addToHand(P, "Torch Fiend", 1);
    addToHand(P, "Disenchant", 1);

    P = opponent;

    P.setLife(1);
    addToLibrary(P, "Plains", 10);
    createPermanent(P, "Rupture Spire", false, 10);
    createPermanent(P, "Mad Auntie", false, 1);
    final MagicPermanent la = createPermanent(P, "Legacy's Allure", false, 1);
    game.doAction(new ChangeCountersAction(P, la, MagicCounterType.Charge, 1));
    //game.doAction(new MagicChangeCountersAction(P, la, MagicCounterType.Charge, 1));
    //createPermanent(P, "Mad Auntie", false, 1);
    // createPermanent(P,"Jayemdae Tome",false,1);

    return game;
    }
}
