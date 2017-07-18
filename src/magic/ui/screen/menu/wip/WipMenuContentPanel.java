package magic.ui.screen.menu.wip;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import magic.data.CardDefinitions;
import magic.model.IRenderableCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicRandom;
import magic.ui.MagicImages;
import magic.ui.ScreenController;
import magic.ui.screen.cardflow.ICardFlowProvider;
import magic.ui.screen.menu.MenuScreenContentPanel;

@SuppressWarnings("serial")
class WipMenuContentPanel extends MenuScreenContentPanel
    implements ICardFlowProvider {

    private List<IRenderableCard> cardFlowCards;

    WipMenuContentPanel() {
        super("WIP Menu", true);
        addMenuItem("Test screen", this::showTestScreen);
        addMenuItem("Cardflow", this::showCardFlowScreen);
        addSpace();
        addSpace();
        addMenuItem("Close menu", this::onCloseMenu);
        refreshMenuLayout();
        mp.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
    }

    private void showCardFlowScreen() {
        ScreenController.showCardFlowScreen(this, "Testing Card Flow");
    }

    private void showTestScreen() {
        ScreenController.showTestScreen();
    }

    private void onCloseMenu() {
        ScreenController.closeActiveScreen(false);
    }

    private List<IRenderableCard> getRandomListOfRenderableCards(int count) {
        List<MagicCardDefinition> sampleCards = new ArrayList<>(CardDefinitions.getDefaultPlayableCardDefs());
        Collections.shuffle(sampleCards, new Random(MagicRandom.nextRNGInt()));
        return sampleCards.stream()
            .limit(count)
            .collect(Collectors.toList());
    }

    private List<IRenderableCard> getCardFlowCards() {
        if (cardFlowCards == null) {
            cardFlowCards = getRandomListOfRenderableCards(200);
        }
        return cardFlowCards;
    }

    @Override
    public BufferedImage getImage(int index) {
        return MagicImages.getCardImage(getCardFlowCards().get(index));
    }

    @Override
    public int getImagesCount() {
        return getCardFlowCards().size();
    }
}
