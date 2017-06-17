package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestAILandSkip extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
    final MagicDuel duel = createDuel();
    final MagicGame game = duel.nextGame();
    game.setPhase(MagicMainPhase.getFirstInstance());
    final MagicPlayer player = game.getPlayer(0);
    final MagicPlayer opponent = game.getPlayer(1);

    MagicPlayer P = player;

    P.setLife(19);
    addToLibrary(P, "Swamp", 10);
    createPermanent(P, "Swamp", false, 2);
    createPermanent(P, "Watery Grave", false, 1);
    createPermanent(P, "Darkslick Shores", false, 1);
    createPermanent(P, "Creeping Tar Pit", false, 1);
    createPermanent(P, "Drowned Catacomb", false, 1);
    createPermanent(P, "Hypnotic Specter", false, 2);
    createPermanent(P, "Shadowmage Infiltrator", false, 2);
    addToHand(P, "Force Spike", 2);
    addToHand(P, "Mana Leak", 1);
    addToHand(P, "Nekrataal", 1);
    addToHand(P, "Go for the Throat", 1);
    addToHand(P, "Royal Assassin", 1);
    addToHand(P, "Counterspell", 1);
    addToGraveyard(P, "Ravenous Rats", 2);
    addToGraveyard(P, "Doom Blade", 1);
    addToGraveyard(P, "Mana Leak", 1);

    P = opponent;

    P.setLife(10);
    addToLibrary(P, "Mountain", 10);
    createPermanent(P, "Mountain", false, 2);
    createPermanent(P, "Blood Crypt", false, 1);
    createPermanent(P, "Skullclamp", false, 1);
    addToHand(P, "Blood Crypt", 2); // originally 5 cards in hand
    addToHand(P, "Wort, Boggart Auntie", 1);
    addToGraveyard(P, "Mountain", 1);
    addToGraveyard(P, "Goblin Arsonist", 1);
    addToGraveyard(P, "Murderous Redcap", 1);
    addToGraveyard(P, "Mogg War Marshal", 1);
    addToGraveyard(P, "Quest for the Gravelord", 1);

    return game;
    }
}
