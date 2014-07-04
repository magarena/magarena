package magic.ui.screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import magic.data.DeckType;
import magic.data.IconImages;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.ui.CardTable;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IDeckConsumer;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.MenuButton;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.viewer.CardViewer;
import magic.ui.viewer.DeckDescriptionViewer;
import magic.ui.viewer.DeckStatisticsViewer;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.deck.DeckPicker;
import magic.ui.widget.deck.DeckStatusPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DeckChooserScreen 
    extends AbstractScreen
    implements IStatusBar, IActionBar {

    private final ScreenContent screenContent;
    private final IDeckConsumer deckConsumer;
    private final DeckStatusPanel deckStatusPanel;

    public DeckChooserScreen(final IDeckConsumer deckConsumer) {
        this.deckConsumer = deckConsumer;
        deckStatusPanel = new DeckStatusPanel();
        screenContent = new ScreenContent();
        setContent(screenContent);
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagStatusBar#getScreenCaption()
     */
    @Override
    public String getScreenCaption() {
        return "Select Deck";
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getLeftAction()
     */
    @Override
    public MenuButton getLeftAction() {
        return MenuButton.getCloseScreenButton("Cancel");
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getRightAction()
     */
    @Override
    public MenuButton getRightAction() {
        return new ActionBarButton(
                "Use Deck", "Click to select this deck.",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        deckConsumer.setDeck(screenContent.getDeck(), screenContent.getDeckPath());
                        getFrame().closeActiveScreen(false);
                    }
                });
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getMiddleActions()
     */
    @Override
    public List<MenuButton> getMiddleActions() {
        final List<MenuButton> buttons = new ArrayList<>();
        buttons.add(
                new ActionBarButton(
                        IconImages.HAND_ICON,
                        "Sample Hand", "See what kind of Hand you might be dealt from this deck.",
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                if (screenContent.getDeck().size() >= 7) {
                                    getFrame().showSampleHandGenerator(screenContent.getDeck());
                                } else {
                                    showInvalidActionMessage("A deck with a minimum of 7 cards is required first.");
                                }
                            }
                        })
                );
        buttons.add(
                new ActionBarButton(
                        IconImages.TILED_ICON,
                        "Deck View", "Shows complete deck using tiled card images.",
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                if (screenContent.getDeck().size() > 0) {
                                    getFrame().showDeckView(screenContent.getDeck());
                                } else {
                                    showInvalidActionMessage("Deck is empty! Nothing to show.");
                                }
                            }
                        })
                );

        return buttons;
    }

    private void showInvalidActionMessage(final String message) {
        JOptionPane.showMessageDialog(this, message, "Invalid Action", JOptionPane.INFORMATION_MESSAGE);
    }

    /* (non-Javadoc)
     * @see magic.ui.MagScreen#canScreenClose()
     */
    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        return true;
    }

    /* (non-Javadoc)
     * @see magic.ui.interfaces.IStatusBar#getStatusPanel()
     */
    @Override
    public JPanel getStatusPanel() {
        return deckStatusPanel;
    }

    private class ScreenContent extends JPanel implements IDeckConsumer {

        private final Theme THEME = ThemeFactory.getInstance().getCurrentTheme();
        private final Color HIGHLIGHT_BACK = THEME.getColor(Theme.COLOR_TITLE_BACKGROUND);
        private final Color HIGHLIGHT_FORE = THEME.getColor(Theme.COLOR_TITLE_FOREGROUND);

        private final CardsListPanel cardsListPanel = new CardsListPanel();
        private MagicDeck selectedDeck = null;
        private Path deckFilePath = null;
        private final DeckDescriptionViewer descViewer = new DeckDescriptionViewer();
        private final CardTable deckTable;
        private final CardViewer cardViewer = new CardViewer(true);
        private final DeckStatisticsViewer statsViewer = new DeckStatisticsViewer();
        private DeckPicker deckPicker;

        private ScreenContent() {

            descViewer.setDeckChooserLayout();
            setOpaque(false);

            selectedDeck = new MagicDeck();
            deckTable = new CardTable(selectedDeck, cardViewer, "{deckName}", true);

            setLayout(new MigLayout("insets 0, gap 0"));
            add(getDeckNamesPanel(), "w 300!, h 100%");
            add(getDeckDetailsPane(), "w 100%, h 100%");
//            add(cardsListPanel, "w 100%, h 100%");
            
        }

        public MagicDeck getDeck() {
            return selectedDeck;
        }

        public Path getDeckPath() {
            return deckFilePath;
        }

        private JSplitPane getDeckDetailsPane() {
            final JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            splitter.setOneTouchExpandable(false);
            splitter.setLeftComponent(deckTable);
            splitter.setRightComponent(getCardDetailsPanel());            
            splitter.setDividerSize(14);
            splitter.setBorder(null);
            splitter.setOpaque(false);
            splitter.getRightComponent().setMinimumSize(new Dimension());
            splitter.setResizeWeight(1.0);                    
            return splitter;
        }

        private JPanel getCardDetailsPanel() {
            final JPanel panel = new JPanel();
            panel.setMinimumSize(new Dimension());
            panel.setOpaque(false);
            panel.setLayout(new MigLayout("insets 0"));
            panel.add(cardViewer, "w 100%, h 0:100%");
//            panel.add(statsViewer);
            return panel;
        }

        private JPanel getDeckNamesPanel() {

            deckPicker = new DeckPicker();
            deckPicker.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));
            deckPicker.addListener(this);

            descViewer.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
            
            // main container panel
            final JPanel container = new TexturedPanel();
            container.setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);
            container.setLayout(new MigLayout("insets 0, gap 0, flowy"));
            container.add(deckPicker, "w 100%, h 100%");
            container.add(descViewer, "w 100%, h 130!");
            return container;
        }

        @Override
        public void setDeck(String deckName, DeckType deckType) {
            System.out.println(deckName + ", " + deckType);
        }

        @Override
        public void setDeck(MagicDeck deck, Path deckPath) {
            selectedDeck = deck;
            deckFilePath = deckPath;
            descViewer.setDeckDescription(deck.getDescription());
            cardsListPanel.refreshList(deck);
            statsViewer.setDeck(deck);
            deckTable.setCards(deck);
            deckTable.setTitle(deck.getName() + " (" + deck.size() + " cards)");
            deckStatusPanel.setDeck(deck, true);
        }

    }

    private class CardsListPanel extends TexturedPanel {

        public CardsListPanel() {
            setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);
            setBorder(FontsAndBorders.BLACK_BORDER);
            setLayout(new MigLayout("flowy, insets 0"));
        }

        public void refreshList(final MagicDeck deck) {

            removeAll();

            final List<MagicCardDefinition> creatures = new ArrayList<>();
            final List<MagicCardDefinition> lands = new ArrayList<>();
            final List<MagicCardDefinition> spells = new ArrayList<>();

            for (MagicCardDefinition cardDef : deck) {
                final String cardType = cardDef.getTypeString();
                if (cardType.toLowerCase().contains("creature")) {
                    creatures.add(cardDef);
                } else if (cardType.toLowerCase().contains("land")) {
                    lands.add(cardDef);
                } else {
                    spells.add(cardDef);
                }
            }

            final JPanel panel = new JPanel(new MigLayout("flowy"));
            panel.setOpaque(false);
            panel.add(getCardTypeListPanel(creatures, "Creatures"));
            panel.add(getCardTypeListPanel(lands, "Lands"));
            panel.add(getCardTypeListPanel(spells, "Spells"));

            final JScrollPane scrollPane = new JScrollPane();
            scrollPane.setViewportView(panel);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            add(scrollPane, "w 100%, h 100%");

            revalidate();
            repaint();

        }

        private JPanel getCardTypeListPanel(final List<MagicCardDefinition> cards, final String caption) {

            final Font cardNameFont = new Font("Monospaced", Font.PLAIN, 16);
            String lastCardName = "";
            int cardTypeCount = 0;

            final JLabel headerLabel = new JLabel("--- " + caption + " ---");
            headerLabel.setForeground(Color.BLACK);
            
            final JPanel cardsList = new JPanel(new MigLayout("flowy"));
            cardsList.setOpaque(false);
            cardsList.add(headerLabel);

            for (MagicCardDefinition card : cards) {
                final String cardName = card.getName();
                if (!cardName.equals(lastCardName) && !lastCardName.isEmpty()) {
                    final JLabel lbl = new JLabel(cardTypeCount + " " + lastCardName);
                    lbl.setFont(cardNameFont);
                    lbl.setForeground(Color.BLACK);
                    cardsList.add(lbl);
                    cardTypeCount = 1;
                } else {
                    cardTypeCount++;
                }
                lastCardName = cardName;
            }

            final JLabel lbl = new JLabel(cardTypeCount + " " + lastCardName);
            lbl.setOpaque(false);
            lbl.setForeground(Color.BLACK);
            lbl.setFont(cardNameFont);
            cardsList.add(lbl);

            return cardsList;
        }

    }

}
