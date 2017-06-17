package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestPopulate extends TestGameBuilder {
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
        addToHand(P, "Eyes in the Skies", 1);
        addToHand(P, "Trostani, Selesnya's Voice", 1);
        addToHand(P, "Vitu-Ghazi Guildmage", 1);
        addToHand(P, "Wayfaring Temple", 1);
        addToHand(P, "Horncaller's Chant", 1);
        addToHand(P, "Druid's Deliverance", 1);

        P = opponent;

        P.setLife(10);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);
        addToHand(P, "Growing Ranks", 1);
        addToHand(P, "Rootborn Defenses", 1);
        addToHand(P, "Sundering Growth", 1);
        addToHand(P, "Trostani's Judgement", 1);
        addToHand(P, "Coursers' Accord", 1);

        return game;
    }
}
