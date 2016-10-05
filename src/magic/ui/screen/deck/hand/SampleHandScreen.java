package magic.ui.screen.deck.hand;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import magic.data.MagicIcon;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicCardList;
import magic.model.MagicDeck;
import magic.model.MagicRandom;
import magic.ui.MagicImages;
import magic.translate.UiString;
import magic.ui.widget.cards.canvas.CardsCanvas.LayoutMode;
import magic.ui.widget.cards.canvas.CardsCanvas;
import magic.cardBuilder.renderers.CardBuilder;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.MenuButton;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.deck.DeckStatusPanel;
import magic.ui.screen.AbstractScreen;
import magic.ui.widget.throbber.AbstractThrobber;
import magic.ui.widget.throbber.ImageThrobber;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class SampleHandScreen
    extends AbstractScreen
    implements IStatusBar, IActionBar {

    // translatable strings
    private static final String _S1 = "Sample Hand";
    private static final String _S2 = "Close";
    private static final String _S3 = "Refresh";
    private static final String _S4 = "Deal a new sample hand.";

    private CardsCanvas content;
    private final MagicDeck deck;
    private final DeckStatusPanel deckStatusPanel = new DeckStatusPanel();
    private AbstractThrobber throbber;

    public SampleHandScreen(final MagicDeck aDeck) {
        this.deck = aDeck;
        if (CardBuilder.IS_LOADED == false && MagicImages.hasProxyImage(aDeck)) {
            setThrobberLayout();
            new ContentWorker(aDeck).execute();
        } else {
            this.setContent(aDeck);
        }
    }

    private void setContent(MagicDeck aDeck) {
        this.content = new CardsCanvas();
        content.setAnimationDelay(50, 20);
        this.content.setLayoutMode(LayoutMode.SCALE_TO_FIT);
        this.content.refresh(getRandomHand(aDeck));
        super.setContent(this.content);
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
            setContent(deck);
            throbber.setVisible(false);
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

    private List<MagicCard> getRandomHand(final MagicDeck deck) {
        final MagicCardList library = new MagicCardList();
        for (MagicCardDefinition magicCardDef : deck) {
            library.add(new MagicCard(magicCardDef, null, 0));
        }
        library.shuffle(MagicRandom.nextRNGInt());
        if (library.size() >= 7) {
            final List<MagicCard> hand = library.subList(0, 7);
            Collections.sort(hand);
            return hand;
        } else {
            return null;
        }
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
                        MagicImages.getIcon(MagicIcon.REFRESH_ICON),
                        UiString.get(_S3), UiString.get(_S4),
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                if (!content.isBusy()) {
                                    content.refresh(getRandomHand(deck));
                                }
                            }
                        })
                );
        return buttons;
    }

    @Override
    public JPanel getStatusPanel() {
        deckStatusPanel.setDeck(deck, false);
        return deckStatusPanel;
    }

}
