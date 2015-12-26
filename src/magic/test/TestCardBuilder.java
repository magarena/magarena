
package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeckProfile;
import magic.model.phase.*;
import magic.ai.MagicAIImpl;
import magic.ui.MagicImages;
import magic.data.CardDefinitions;

class TestCardBuilder extends TestGameBuilder {
    public MagicGame getGame() {
        for (final MagicCardDefinition cdef : CardDefinitions.getAllCards()) {
            System.out.println(cdef);
            if (cdef.getName().equals("Rage Extractor")) {
                System.out.println("skipping");
                continue;
            }
            MagicImages.getMissingCardImage(cdef);
        }
        final MagicDuel duel=createDuel(MagicAIImpl.MCTS, 6);
        final MagicGame game=duel.nextGame();
        return game;
    }
}
