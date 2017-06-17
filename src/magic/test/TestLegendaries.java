package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestLegendaries extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(9);
        addToLibrary(P, "Plains", 10);
        createPermanent(P, "Karakas", false, 1);
        createPermanent(P, "Kor Haven", false, 1);
        createPermanent(P, "Maze of Ith", false, 1);
        createPermanent(P, "Mikokoro, Center of the Sea", false, 1);
        createPermanent(P, "Miren, the Moaning Well", false, 1);
        createPermanent(P, "Okina, Temple to the Grandfathers", false, 1);
        createPermanent(P, "Azusa, Lost but Seeking", false, 1);
        createPermanent(P, "Captain Sisay", false, 1);
        createPermanent(P, "Knight of the Reliquary", false, 1);
        createPermanent(P, "Kokusho, the Evening Star", false, 1);
        addToHand(P, "Kor Haven", 1);
        addToHand(P, "Maze of Ith", 1);
        addToHand(P, "Mikokoro, Center of the Sea", 1);
        addToHand(P, "Miren, the Moaning Well", 1);
        addToHand(P, "Okina, Temple to the Grandfathers", 1);


        P = opponent;

        P.setLife(9);
        addToLibrary(P, "Plains", 10);
        createPermanent(P, "Karakas", false, 1);
        createPermanent(P, "Kor Haven", false, 1);
        createPermanent(P, "Maze of Ith", false, 1);
        createPermanent(P, "Mikokoro, Center of the Sea", false, 1);
        createPermanent(P, "Miren, the Moaning Well", false, 1);
        createPermanent(P, "Okina, Temple to the Grandfathers", false, 1);
        createPermanent(P, "Azusa, Lost but Seeking", false, 1);
        createPermanent(P, "Captain Sisay", false, 1);
        createPermanent(P, "Knight of the Reliquary", false, 1);
        createPermanent(P, "Kokusho, the Evening Star", false, 1);
        addToHand(P, "Kor Haven", 1);
        addToHand(P, "Maze of Ith", 1);
        addToHand(P, "Mikokoro, Center of the Sea", 1);
        addToHand(P, "Miren, the Moaning Well", 1);
        addToHand(P, "Okina, Temple to the Grandfathers", 1);

        return game;
    }
}
