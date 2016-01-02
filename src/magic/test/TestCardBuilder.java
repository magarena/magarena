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

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

class TestCardBuilder extends TestGameBuilder {
    public MagicGame getGame() {
        for (final MagicCardDefinition cdef : CardDefinitions.getAllCards()) {
            final BufferedImage buffImg = MagicImages.getMissingCardImage(cdef);
            int[] data = ((DataBufferInt) buffImg.getData().getDataBuffer()).getData();

            int hash = Arrays.hashCode(data);
            System.out.println(cdef.getDistinctName() + "\t" + hash);
        }
        final MagicDuel duel=createDuel(MagicAIImpl.MCTS, 6);
        final MagicGame game=duel.nextGame();
        return game;
    }
}
