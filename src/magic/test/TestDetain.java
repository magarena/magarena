
package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestDetain extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(1);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);
        createPermanent(P,"Arbor Elf",false,1);
        addToHand(P, "Azorius Arrester", 1);
        addToHand(P, "Isperia's Skywatch", 1);
        addToHand(P, "Soulsworn Spirit", 1);
        addToHand(P, "Inaction Injunction", 1);
        addToHand(P, "Lyev Skyknight", 1);
        addToHand(P, "Martial Law", 1);
        addToHand(P, "New Prahv Guildmage", 1);

        P = opponent;

        P.setLife(1);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,1);
        createPermanent(P,"Forest",true,1);
        createPermanent(P,"Shivan Hellkite",false,1);

        return game;
    }
}
