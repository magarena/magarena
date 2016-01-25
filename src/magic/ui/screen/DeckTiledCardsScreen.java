package magic.ui.screen;

import magic.ui.MagicImages;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.model.MagicType;
import magic.ui.canvas.cards.CardsCanvas;
import magic.ui.canvas.cards.CardsCanvas.LayoutMode;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.MenuButton;
import magic.ui.screen.widget.SampleHandActionButton;
import net.miginfocom.swing.MigLayout;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.swing.SwingWorker;
import magic.data.MagicIcon;
import magic.translate.UiString;
import magic.translate.StringContext;
import magic.ui.canvas.cards.ICardsCanvasListener;
import magic.ui.cardBuilder.renderers.CardBuilder;
import magic.ui.deck.editor.DeckEditorSideBarPanel;
import magic.ui.prefs.ImageSizePresets;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.throbber.AbstractThrobber;
import magic.ui.widget.throbber.ImageThrobber;

@SuppressWarnings("serial")
public class DeckTiledCardsScreen
    extends AbstractScreen
    implements IStatusBar, IActionBar {

    // translatable strings
    private static final String _S1 = "Deck View";
    private static final String _S2 = "Close";
    private static final String _S3 = "All";
    private static final String _S4 = "Display all cards in deck.";
    private static final String _S5 = "Creatures";
    private static final String _S6 = "Display only creature cards.";
    private static final String _S7 = "Lands";
    private static final String _S8 = "Display only land cards.";
    private static final String _S9 = "Other Spells";
    private static final String _S10 = "Display any other card that is not a creature or land.";
    @StringContext(eg = "Creatures (28 cards, 46%)")
    private static final String _S11 = "%s (%d cards, %d%%)";
    @StringContext(eg = "All cards (60 cards)")
    private static final String _S12 = "%s (%d cards)";

    private enum CardTypeFilter {
        ALL("All cards"),
        CREATURES("Creatures"),
        LANDS("Lands"),
        OTHER("Other Spells");
        private final String caption;
        private CardTypeFilter(final String caption) {
            this.caption = caption;
        }
        @Override
        public String toString() {
            return caption;
        }
    }

    private final static Dimension cardSize = ImageSizePresets.getDefaultSize();

    private ContentPanel content;
    private final MagicDeck deck;
    private final StatusPanel statusPanel;
    private AbstractThrobber throbber;

    public DeckTiledCardsScreen(final MagicDeck aDeck) {
        this.deck = aDeck;
        this.statusPanel = new StatusPanel(aDeck.getName(), getCardTypeCaption(CardTypeFilter.ALL, aDeck.size()));
        if (CardBuilder.IS_LOADED == false && MagicImages.hasProxyImage(aDeck)) {
            setThrobberLayout();
            new ContentWorker(aDeck).execute();
        } else {
            content = new ContentPanel();
            setContent(content);
        }        
    }

    private void setThrobberLayout() {
        throbber = new ImageThrobber.Builder(MagicImages.loadImage("round-shield.png")).build();
        JLabel progressLabel = new JLabel();
        progressLabel.setFont(FontsAndBorders.FONT2);
        progressLabel.setForeground(Color.WHITE);
        progressLabel.setText("<html><center>This deck contains one or more proxy images.<br>Please wait while the proxy image generator is initialized.</center></html>");
        setLayout(new MigLayout("flowy, aligny center, alignx center"));
        add(throbber, "alignx center");
        add(progressLabel);
    }

    private class ContentWorker extends SwingWorker<Void, String> {

        private final MagicDeck deck;

        public ContentWorker(MagicDeck aDeck) {
            this.deck = aDeck;
        }

        @Override
        protected Void doInBackground() throws Exception {
            for (MagicCardDefinition aCard : deck) {
                if (MagicImages.isProxyImage(aCard)) {
                    CardBuilder.getCardBuilderImage(aCard);
                    break;
                }
            }
            return null;
        }

        @Override
        protected void done() {
            content = new ContentPanel();
            setContent(content);
            throbber.setVisible(false);
        }

    }

    private class ContentPanel extends JPanel implements ICardsCanvasListener {

        private final DeckEditorSideBarPanel sidebar;
        private final CardsCanvas canvas;

        public ContentPanel() {

            sidebar = new DeckEditorSideBarPanel();
            sidebar.getStatsViewer().setDeck(deck);

            canvas = new CardsCanvas(cardSize);
            canvas.setListener(this);
            canvas.setAnimationEnabled(false);
            canvas.setStackDuplicateCards(true);
            canvas.setLayoutMode(LayoutMode.SCALE_TO_FIT);
            canvas.refresh(getFilteredDeck(deck, CardTypeFilter.ALL), cardSize);

            setOpaque(false);
            setLayout(new MigLayout("insets 0, gap 0"));
            refreshLayout();

            sidebar.setCard(deck.isEmpty()
                ? MagicCardDefinition.UNKNOWN
                : deck.get(0)
            );

        }

        public void refreshLayout() {
            removeAll();
            add(sidebar, "h 100%");
            add(canvas, "w 100%, h 100%");
            revalidate();
        }

        public void refresh(List<MagicCard> cards, Dimension cardSize) {
            sidebar.setCard(cards.isEmpty()
                ? MagicCardDefinition.UNKNOWN
                : cards.get(0).getCardDefinition()
            );
            canvas.refresh(cards, cardSize);
        }

        @Override
        public void cardSelected(MagicCard aCard) {
            sidebar.setCard(aCard.getCardDefinition());
        }

    }

    private List<MagicCard> getFilteredDeck(final MagicDeck deck, final CardTypeFilter filterType) {

        final List<MagicCard> cards = new ArrayList<>();

        for (MagicCardDefinition cardDef : deck) {

            final Set<MagicType> cardType = cardDef.getCardType();
            final MagicCard card = new MagicCard(cardDef, null, 0);

            switch (filterType) {
                case CREATURES:
                    if (cardType.contains(MagicType.Creature)) {
                        cards.add(card);
                    }
                    break;
                case LANDS:
                    if (cardType.contains(MagicType.Land)) {
                        cards.add(card);
                    }
                    break;
                case OTHER:
                    if (!cardType.contains(MagicType.Creature) && !cardType.contains(MagicType.Land)) {
                        cards.add(card);
                    }
                    break;
                default: // ALL
                    cards.add(card);
                    break;
            }

        }
        Collections.sort(cards);
        return cards;
    }

    @Override
    public String getScreenCaption() {
        return UiString.get(_S1);
    }

    @Override
    public MenuButton getLeftAction() {
        return MenuButton.getCloseScreenButton(UiString.get(_S2));
    }

    @Override
    public MenuButton getRightAction() {
        return null;
    }

    @Override
    public List<MenuButton> getMiddleActions() {
        final List<MenuButton> buttons = new ArrayList<>();
        buttons.add(
                new ActionBarButton(
                        UiString.get(_S3), UiString.get(_S4),
                        new ShowCardsAction(CardTypeFilter.ALL), false));
        buttons.add(new ActionBarButton(
                        MagicImages.getIcon(MagicIcon.CREATURES_ICON),
                        UiString.get(_S5), UiString.get(_S6),
                        new ShowCardsAction(CardTypeFilter.CREATURES), false)
                );
        buttons.add(new ActionBarButton(
                        MagicImages.getIcon(MagicIcon.LANDS_ICON),
                        UiString.get(_S7), UiString.get(_S8),
                        new ShowCardsAction(CardTypeFilter.LANDS), false)
                );
        buttons.add(new ActionBarButton(
                        MagicImages.getIcon(MagicIcon.SPELLS_ICON),
                        UiString.get(_S9), UiString.get(_S10),
                        new ShowCardsAction(CardTypeFilter.OTHER), true)
                );
        buttons.add(SampleHandActionButton.createInstance(deck));
        return buttons;
    }

    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        return true;
    }

    @Override
    public JPanel getStatusPanel() {
        return statusPanel;
    }

    private class ShowCardsAction extends AbstractAction {

        private final CardTypeFilter filter;

        public ShowCardsAction(final CardTypeFilter filter) {
            this.filter = filter;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            showCards(filter);
        }

        private void showCards(final CardTypeFilter filterType) {
            final List<MagicCard> cards = getFilteredDeck(deck, filterType);
            content.refresh(cards, cardSize);
            statusPanel.setContent(deck.getName(), getCardTypeCaption(filterType, cards == null ? 0 : cards.size()));
        }

    }

    private String getCardTypeCaption(final CardTypeFilter cardType, final int cardCount) {
        if (cardType != CardTypeFilter.ALL) {
            final int percentage = (int)((cardCount / (double)deck.size()) * 100);
            return UiString.get(_S11, cardType, cardCount, percentage);
        } else {
            return UiString.get(_S12, cardType, cardCount);
        }
    }

     private final class StatusPanel extends JPanel {

        // ui
        private final MigLayout migLayout = new MigLayout();
        private final JLabel deckNameLabel = new JLabel();
        private final JLabel filterLabel = new JLabel();

        public StatusPanel(final String deckName, final String filterCaption) {
            setLookAndFeel();
            setContent(deckName, filterCaption);
        }

        private void setLookAndFeel() {
            setOpaque(false);
            setLayout(migLayout);
            // deck name label
            deckNameLabel.setForeground(Color.WHITE);
            deckNameLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
            deckNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            // filter label
            filterLabel.setForeground(Color.WHITE);
            filterLabel.setFont(new Font("Dialog", Font.ITALIC, 14));
            filterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }

        private void refreshLayout() {
            removeAll();
            migLayout.setLayoutConstraints("insets 0, gap 2, flowy");
            add(deckNameLabel, "w 100%");
            add(filterLabel, "w 100%");
        }

        public void setContent(final String deckName, final String filterCaption) {
            deckNameLabel.setText(deckName);
            filterLabel.setText(filterCaption);
            refreshLayout();
        }

    }

}
