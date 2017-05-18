package magic.ui.screen.deck.tiled;

import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.JPanel;
import magic.model.IRenderableCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.ui.MagicImages;
import magic.ui.ScreenController;
import magic.ui.screen.deck.editor.DeckSideBar;
import magic.ui.screen.cardflow.ICardFlowProvider;
import magic.ui.widget.cards.canvas.CardsCanvas;
import magic.ui.widget.cards.canvas.ICardsCanvasListener;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ContentPanel extends JPanel
    implements ICardsCanvasListener, ICardFlowProvider {

    private final DeckSideBar sidebar;
    private final CardsCanvas canvas;
    private List<? extends IRenderableCard> cards;

    ContentPanel(final MagicDeck aDeck) {

        this.cards = aDeck;

        sidebar = new DeckSideBar();
        sidebar.setDeck(aDeck);

        canvas = new CardsCanvas();
        canvas.setListener(this);
        canvas.setAnimationEnabled(false);
        canvas.setStackDuplicateCards(true);
        canvas.setLayoutMode(CardsCanvas.LayoutMode.SCALE_TO_FIT);

        setOpaque(false);
        setLayout(new MigLayout("insets 0, gap 0"));
        refreshLayout();

        sidebar.setCard(aDeck.isEmpty()
                ? MagicCardDefinition.UNKNOWN
                : aDeck.get(0)
        );

    }

    void refreshLayout() {
        removeAll();
        add(sidebar, "h 100%");
        add(canvas, "w 100%, h 100%");
        revalidate();
    }

    void refresh(List<MagicCardDefinition> cards) {
        this.cards = cards;
        sidebar.setCard(cards.isEmpty()
            ? MagicCardDefinition.UNKNOWN
            : cards.get(0)
        );
        canvas.refresh(cards);
    }

    @Override
    public void cardSelected(MagicCardDefinition aCard) {
        sidebar.setCard(aCard);
    }

    private int startImageIndex = 0;

    @Override
    public void cardClicked(int index, MagicCardDefinition card) {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i) == card) {
                startImageIndex = i;
                ScreenController.showCardFlowScreen(this, "Deck");
                break;
            }
        }
    }

    @Override
    public BufferedImage getImage(int index) {
        return MagicImages.getCardImage(cards.get(index));
    }

    @Override
    public int getImagesCount() {
        return cards.size();
    }

    @Override
    public int getStartImageIndex() {
        return startImageIndex;
    }

}
