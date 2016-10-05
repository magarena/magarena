package magic.ui;

import java.awt.AlphaComposite;
import magic.translate.UiString;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import magic.data.DeckType;
import magic.data.DuelConfig;
import magic.data.MagicIcon;
import magic.exception.InvalidDeckException;
import magic.model.DuelPlayerConfig;
import magic.model.MagicDeck;
import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.player.PlayerProfile;
import magic.ui.cardtable.CardTable;
import magic.ui.screen.deck.editor.DeckSideBar;
import magic.ui.player.PlayerDetailsPanel;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.MenuButton;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DuelDecksPanel extends TexturedPanel {

    // translatable strings
    private static final String _S2 = "Deck (%s) - %d cards";
    private static final String _S7 = "Swap Decks";
    private static final String _S8 = "Swap your deck with your opponent's.";
    private static final String _S15 = "Generate another deck";
    private static final String _S16 = "Based on the duel settings, randomly selects an existing prebuilt deck or<br>generates a random deck for the selected player.";

    // change properties
    public static final String CP_DECK_CHANGED = "1fe41854-83e4-4a98-9c4b-46ca9f4c9550";

    private final MigLayout migLayout = new MigLayout();
    private final MagicDuel duel;
    private final JTabbedPane tabbedPane;
    private final CardTable[] cardTables;
    private final DeckSideBar sidebar;
    private final ActionBarButton newDeckButton;

    public DuelDecksPanel(final MagicDuel duel) {

        this.duel = duel;
        newDeckButton = getNewDeckActionBarButton();

        setBackground(FontsAndBorders.MAGSCREEN_FADE_COLOR);

        sidebar = new DeckSideBar();

        // create tabs for each player
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);

        final DuelPlayerConfig[] players = duel.getPlayers();
        cardTables = new CardTable[players.length];

        for (int i = 0; i < players.length; i++) {

            final DuelPlayerConfig player = players[i];

            cardTables[i] = new CardTable(player.getDeck(), generateTitle(player.getDeck()), true);
            cardTables[i].addCardSelectionListener(sidebar.getCardViewer());
            cardTables[i].showCardCount(true);

            final JPanel tabPanel = new JPanel(new MigLayout("insets 0"));
            tabPanel.add(cardTables[i], "w 100%, h 100%");
            tabbedPane.addTab(null, tabPanel);

            final DuelConfig duelConfig = duel.getConfiguration();
            final PlayerProfile profile = duelConfig.getPlayerProfile(i);
            tabbedPane.setTabComponentAt(i, new PlayerPanel(profile));
        }

        tabbedPane.addChangeListener((ChangeEvent e) -> {
            int newIndex = tabbedPane.getSelectedIndex();
            int oldIndex = newIndex == 0 ? 1 : 0;
            ((PlayerPanel) tabbedPane.getTabComponentAt(newIndex)).setSelected(true);
            ((PlayerPanel) tabbedPane.getTabComponentAt(oldIndex)).setSelected(false);
            setDeck();
        });

        tabbedPane.setPreferredSize(new Dimension(800, 0));

        // layout screen components.
        migLayout.setLayoutConstraints("insets 0, gap 0");
        setLayout(migLayout);
        add(sidebar, "h 100%");
        add(tabbedPane, "h 100%, w 100%");

        ((PlayerPanel) tabbedPane.getTabComponentAt(1)).setSelected(false);

        setDeck();
    }

    private void setDeck() {
        DuelPlayerConfig player = getSelectedPlayer();
        MagicDeck deck = player.getDeck();
        sidebar.setDeck(deck);
        sidebar.setCard(deck.get(0));
        boolean isRandomDeck = player.getDeckProfile().getDeckType() == DeckType.Random;
        newDeckButton.setEnabled(isRandomDeck && duel.getGamesPlayed() == 0);
    }

    String generateTitle(final MagicDeck deck) {
        return UiString.get(_S2, deck.getName(), deck.size());
    }

    public MagicDuel getDuel() {
        return duel;
    }

    public DuelPlayerConfig getSelectedPlayer() {
        return duel.getPlayers()[tabbedPane.getSelectedIndex()];
    }

    public void setSelectedTab(final int tab) {
        tabbedPane.setSelectedIndex(tab);
    }

    public void updateDecksAfterEdit() {
        for (int i = 0; i < duel.getPlayers().length; i++) {
            final DuelPlayerConfig player = duel.getPlayers()[i];
            final MagicDeck deck = player.getDeck();
            cardTables[i].setCards(deck);
            cardTables[i].setTitle(generateTitle(deck));
        }
        setDeck();
        firePropertyChange(CP_DECK_CHANGED, true, false);
    }

    @SuppressWarnings("serial")
    private class PlayerPanel extends JPanel {

        private boolean isSelected = true;

        public PlayerPanel(final PlayerProfile profile) {
            setLayout(new MigLayout("insets 0"));
            setOpaque(false);
            add(new JLabel(MagicImages.getPlayerAvatar(profile).getIcon(4)));
            add(new PlayerDetailsPanel(profile, Color.BLACK), "w 100%");
            add(getScoreLabel(getScore(profile)), "w 100%");
            setPreferredSize(new Dimension(280, 54));
        }

        private int getScore(final PlayerProfile profile) {
            return profile.isHuman()
                    ? duel.getGamesWon()
                    : duel.getGamesPlayed() - duel.getGamesWon();
        }

        private JLabel getScoreLabel(final int score) {
            final JLabel lbl = new JLabel(Integer.toString(score));
            lbl.setFont(new Font("Dialog", Font.PLAIN, 24));
            lbl.setHorizontalAlignment(SwingConstants.RIGHT);
            return lbl;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!isSelected) {
                final Graphics2D g2d = (Graphics2D) g;
                final Composite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
                g2d.setComposite(composite);
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        }

        private void setSelected(boolean b) {
            isSelected = b;
            repaint();
        }

    }

    public void generateDeck() {
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            duel.buildDeck(getSelectedPlayer());
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } catch (InvalidDeckException ex) {
            ScreenController.showWarningMessage(ex.getMessage());
        }
        updateDecksAfterEdit();
    }

    private ActionBarButton getNewDeckActionBarButton() {
        return new ActionBarButton(
            MagicImages.getIcon(MagicIcon.RANDOM_ICON),
            UiString.get(_S15),
            UiString.get(_S16),
            new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    generateDeck();
                }
            }
        );
    }

    public void swapDecks() {
        duel.restart();
        final DuelPlayerConfig[] players = duel.getPlayers();
        final MagicDeckProfile deckProfile1 = players[0].getDeckProfile();
        final MagicDeckProfile deckProfile2 = players[1].getDeckProfile();
        final MagicDeck deck1 = new MagicDeck(players[0].getDeck());
        final MagicDeck deck2 = new MagicDeck(players[1].getDeck());
        players[0].setDeckProfile(deckProfile2);
        players[0].setDeck(deck2);
        players[1].setDeckProfile(deckProfile1);
        players[1].setDeck(deck1);
        ScreenController.closeActiveScreen(false);
        ScreenController.getMainFrame().showDuel();
    }

    private ActionBarButton getSwapDecksButton() {
        return new ActionBarButton(
            MagicImages.getIcon(MagicIcon.SWAP_ICON),
            UiString.get(_S7), UiString.get(_S8),
            new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    try {
                        swapDecks();
                    } catch (InvalidDeckException ex) {
                        ScreenController.showWarningMessage(ex.getMessage());
                    }
                }
            }
        );
    }

    public Collection<? extends MenuButton> getActionBarButtons() {
        final List<MenuButton> buttons = new ArrayList<>();
        buttons.add(newDeckButton);
        buttons.add(getSwapDecksButton());
        return buttons;
    }

}
