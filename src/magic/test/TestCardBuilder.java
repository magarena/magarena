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
import magic.cardBuilder.renderers.CardBuilder;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.imageio.ImageIO;
import java.util.Arrays;
import java.io.File;
import java.io.IOException;

class TestCardBuilder extends TestGameBuilder {
    public MagicGame getGame() {
        for (final MagicCardDefinition cdef : CardDefinitions.getAllCards()) {
            final BufferedImage buffImg = CardBuilder.getCardBuilderImage(cdef);
            int[] data = ((DataBufferInt) buffImg.getData().getDataBuffer()).getData();

            int hash = Arrays.hashCode(data);
            System.out.println(cdef.getDistinctName() + "\t" + hash);

            if (cdef.getDistinctName().contains("QQQ")) {
                try {
                    File outputfile = new File(cdef.getImageName() + ".png");
                    ImageIO.write(buffImg, "png", outputfile);
                } catch (IOException e) {
                    System.out.println("saving " + cdef.getDistinctName() + " failed");
                }
            }
        }
        final MagicDuel duel=createDuel(MagicAIImpl.MCTS, 6);
        final MagicGame game=duel.nextGame();
        return game;
    }
}
