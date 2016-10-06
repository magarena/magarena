package magic.ui.screen.deck.tiled;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import magic.data.MagicIcon;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.model.MagicType;
import magic.translate.StringContext;
import magic.translate.UiString;
import magic.ui.MagicImages;
import magic.ui.widget.cards.canvas.CardsCanvas.LayoutMode;
import magic.ui.widget.cards.canvas.CardsCanvas;
import magic.ui.widget.cards.canvas.ICardsCanvasListener;
import magic.cardBuilder.renderers.CardBuilder;
import magic.ui.screen.AbstractScreen;
import magic.ui.screen.deck.editor.DeckSideBar;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.MenuButton;
import magic.ui.screen.widget.SampleHandActionButton;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.throbber.AbstractThrobber;
import magic.ui.widget.throbber.ImageThrobber;
import net.miginfocom.swing.MigLayout;

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
    private static final String _S9 = "Artifacts";
    private static final String _S10 = "Display only artifact cards.";
    @StringContext(eg = "Creatures (28 cards, 46%)")
    private static final String _S11 = "%s (%d cards, %d%%)";
    @StringContext(eg = "All cards (60 cards)")
    private static final String _S12 = "%s (%d cards)";
    private static final String _S13 = "Enchantments";
    private static final String _S14 = "Display only enchantment cards.";
    private static final String _S15 = "Instants";
    private static final String _S16 = "Display only instant cards.";
    private static final String _S17 = "Sorceries";
    private static final String _S18 = "Display only sorcery cards.";
    private static final String _S19 = "Planeswalkers";
    private static final String _S20 = "Display only planeswalker cards.";
    private static final String _S21 = "All cards";
    private static final String _S22 = "This deck contains one or more proxy images.";
    private static final String _S23 = "Please wait while the proxy image generator is initialized.";

    private enum CardTypeFilter {
        ALL(_S21),
        CREATURES(_S5),
        LANDS(_S7),
        ARTIFACTS(_S9),
        ENCHANTMENTS(_S13),
        INSTANTS(_S15),
        SORCERIES(_S17),
        PLANESWALKERS(_S19);

        private final String caption;

        private CardTypeFilter(final String caption) {
            this.caption = UiString.get(caption);
        }

        @Override
        public String toString() {
            return caption;
        }
    }

    private ContentPanel content;
    private final MagicDeck deck;
    private final StatusPanel statusPanel;
    private AbstractThrobber throbber;

    public DeckTiledCardsScreen(final MagicDeck aDeck) {
        deck = aDeck;
        statusPanel = new StatusPanel(aDeck.getName(), getCardTypeCaption(CardTypeFilter.ALL, aDeck.size()));
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
        progressLabel.setText(String.format("<html><center>%s<br>%s</center></html>",
            UiString.get(_S22),
            UiString.get(_S23))
        );
        setLayout(new MigLayout("flowy, aligny center, alignx center"));
        add(throbber, "alignx center");
        add(progressLabel);
    }

    private class ContentWorker extends SwingWorker<Void, String> {

        private final MagicDeck deck;

        public ContentWorker(MagicDeck aDeck) {
            deck = aDeck;
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

        private final DeckSideBar sidebar;
        private final CardsCanvas canvas;

        public ContentPanel() {

            sidebar = new DeckSideBar();
            sidebar.setDeck(deck);

            canvas = new CardsCanvas();
            canvas.setListener(this);
            canvas.setAnimationEnabled(false);
            canvas.setStackDuplicateCards(true);
            canvas.setLayoutMode(LayoutMode.SCALE_TO_FIT);
            canvas.refresh(getFilteredDeck(deck, CardTypeFilter.ALL));

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

        public void refresh(List<MagicCard> cards) {
            sidebar.setCard(cards.isEmpty()
                ? MagicCardDefinition.UNKNOWN
                : cards.get(0).getCardDefinition()
            );
            canvas.refresh(cards);
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
                case ALL:
                    cards.add(card);
                    break;
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
                case ARTIFACTS:
                    if (cardType.contains(MagicType.Artifact)) {
                        cards.add(card);
                    }
                    break;
                case ENCHANTMENTS:
                    if (cardType.contains(MagicType.Enchantment)) {
                        cards.add(card);
                    }
                    break;
                case INSTANTS:
                    if (cardType.contains(MagicType.Instant)) {
                        cards.add(card);
                    }
                    break;
                case SORCERIES:
                    if (cardType.contains(MagicType.Sorcery)) {
                        cards.add(card);
                    }
                    break;
                case PLANESWALKERS:
                    if (cardType.contains(MagicType.Planeswalker)) {
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
        buttons.add(new ActionBarButton(
            UiString.get(_S3), UiString.get(_S4),
            new ShowCardsAction(CardTypeFilter.ALL), false)
        );
        if (deck.contains(MagicType.Land)) {
            buttons.add(new ActionBarButton(
                MagicImages.getIcon(MagicIcon.LANDS),
                UiString.get(_S7), UiString.get(_S8),
                new ShowCardsAction(CardTypeFilter.LANDS), false)
            );
        }
        if (deck.contains(MagicType.Creature)) {
            buttons.add(new ActionBarButton(
                MagicImages.getIcon(MagicIcon.CREATURES),
                UiString.get(_S5), UiString.get(_S6),
                new ShowCardsAction(CardTypeFilter.CREATURES), false)
            );
        }
        if (deck.contains(MagicType.Artifact)) {
            buttons.add(new ActionBarButton(
                MagicImages.getIcon(MagicIcon.ARTIFACTS),
                UiString.get(_S9), UiString.get(_S10),
                new ShowCardsAction(CardTypeFilter.ARTIFACTS), false)
            );
        }
        if (deck.contains((MagicType.Enchantment))) {
            buttons.add(new ActionBarButton(
                MagicImages.getIcon(MagicIcon.ENCHANTMENTS),
                UiString.get(_S13), UiString.get(_S14),
                new ShowCardsAction(CardTypeFilter.ENCHANTMENTS), false)
            );
        }
        if (deck.contains(MagicType.Instant)) {
            buttons.add(new ActionBarButton(
                MagicImages.getIcon(MagicIcon.INSTANTS),
                UiString.get(_S15), UiString.get(_S16),
                new ShowCardsAction(CardTypeFilter.INSTANTS), false)
            );
        }
        if (deck.contains(MagicType.Sorcery)) {
            buttons.add(new ActionBarButton(
                MagicImages.getIcon(MagicIcon.SORCERIES),
                UiString.get(_S17), UiString.get(_S18),
                new ShowCardsAction(CardTypeFilter.SORCERIES), false)
            );
        }
        if (deck.contains(MagicType.Planeswalker)) {
            buttons.add(new ActionBarButton(
                MagicImages.getIcon(MagicIcon.PLANESWALKERS),
                UiString.get(_S19), UiString.get(_S20),
                new ShowCardsAction(CardTypeFilter.PLANESWALKERS), true)
            );
        }
        buttons.get(buttons.size()-1).setSeparator(true);
        buttons.add(SampleHandActionButton.createInstance(deck));
        return buttons;
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
            content.refresh(cards);
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
