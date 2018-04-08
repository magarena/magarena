package magic.test;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
import magic.ai.MagicAIImpl;
import magic.cardBuilder.renderers.CardBuilder;
import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;
import magic.model.MagicDuel;
import magic.model.MagicGame;

class TestCardBuilder extends TestGameBuilder {
    @Override
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
        return duel.nextGame();
    }
}
