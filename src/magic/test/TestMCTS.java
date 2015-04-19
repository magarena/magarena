package magic.test;

import magic.ai.MagicAI;
import magic.ai.MagicAIImpl;
import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;

import magic.model.phase.MagicMainPhase;

class TestMCTS extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(4);
        addToLibrary(P, "Plains", 10);
        createPermanent(game,P,"Rupture Spire",false,8);
        createPermanent(game,P,"Hearthfire Hobgoblin", false, 4);
        createPermanent(game,P,"Akrasan Squire", false, 4);


        P = opponent;

        P.setLife(4);
        addToLibrary(P, "Plains", 10);
        createPermanent(game,P,"Rupture Spire",false,8);
        createPermanent(game,P,"Hearthfire Hobgoblin", false, 4);
        createPermanent(game,P,"Akrasan Squire", false, 4);

        return game;
    }
}
