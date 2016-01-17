package magic.ui;

import magic.translate.UiString;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import magic.data.DeckType;
import magic.data.DuelConfig;
import magic.exception.InvalidDeckException;
import magic.model.DuelPlayerConfig;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.model.MagicDuel;
import magic.model.player.PlayerProfile;
import magic.ui.cardtable.CardTable;
import magic.ui.duel.viewer.CardViewer;
import magic.ui.duel.viewer.DeckDescriptionViewer;
import magic.ui.duel.viewer.DeckStatisticsViewer;
import magic.ui.player.PlayerDetailsPanel;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DuelDecksPanel extends TexturedPanel {

    // translatable strings
    private static final String _S1 = "Generate Deck";
    private static final String _S2 = "Deck (%s) - %d cards";

    // change properties
    public static final String CP_DECK_CHANGED = "c832e918-5b52";

    private static final int SPACING = 10;
    private static final String GENERATE_BUTTON_TEXT = UiString.get(_S1);

    private final MigLayout migLayout = new MigLayout();
    private final MagicDuel duel;
    private final JTabbedPane tabbedPane;
    private final DeckDescriptionViewer[] deckDescriptionViewers;
    private final CardViewer cardViewer;
    private final CardTable[] cardTables;
    private final JButton[] generateButtons;
    private final DeckStatisticsViewer[] statsViewers;

    public DuelDecksPanel(final MagicDuel duel) {

        this.duel=duel;

        setBackground(FontsAndBorders.MAGSCREEN_FADE_COLOR);

        // buttons
        final JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.setOpaque(false);

        // card image
        cardViewer=new CardViewer();
        cardViewer.setCard(MagicCardDefinition.UNKNOWN);
        cardViewer.setAlignmentX(Component.LEFT_ALIGNMENT);

        // add scrolling to left side
        final JScrollPane leftScrollPane = new JScrollPane(cardViewer);
        leftScrollPane.setBorder(FontsAndBorders.NO_BORDER);
        leftScrollPane.setOpaque(false);
        leftScrollPane.getViewport().setOpaque(false);
        leftScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        leftScrollPane.setMinimumSize(new Dimension(cardViewer.getMinimumSize().width, 0));
        
        // create tabs for each player
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);

        final DuelPlayerConfig[] players = duel.getPlayers();
        cardTables = new CardTable[players.length];
        deckDescriptionViewers = new DeckDescriptionViewer[players.length];
        statsViewers = new DeckStatisticsViewer[players.length];
        generateButtons = new JButton[players.length];

        for (int i = 0; i < players.length; i++) {

            final DuelPlayerConfig player = players[i];

            // deck description
            deckDescriptionViewers[i] = new DeckDescriptionViewer();
            deckDescriptionViewers[i].setPlayer(player);
            deckDescriptionViewers[i].setAlignmentX(Component.LEFT_ALIGNMENT);

            // deck statistics
            statsViewers[i] = new DeckStatisticsViewer();
            statsViewers[i].setDeck(player.getDeck());
            statsViewers[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            statsViewers[i].setMaximumSize(DeckStatisticsViewer.PREFERRED_SIZE);

            // generate deck button
            generateButtons[i] = new JButton(GENERATE_BUTTON_TEXT);
            generateButtons[i].setFont(FontsAndBorders.FONT2);
            generateButtons[i].setEnabled(duel.getGamesPlayed() == 0 && player.getDeckProfile().getDeckType() == DeckType.Random);
            generateButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent event) {
                    try {
                        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        duel.buildDeck(player);
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    } catch (InvalidDeckException ex) {
                        ScreenController.showWarningMessage(ex.getMessage());
                    }
                    updateDecksAfterEdit();
                }
            });

            // right side
            final JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            rightPanel.setOpaque(false);

            rightPanel.add(statsViewers[i]);
            rightPanel.add(Box.createVerticalStrut(SPACING));

            rightPanel.add(deckDescriptionViewers[i]);
            rightPanel.add(Box.createVerticalStrut(SPACING));

            if (player.getProfile().isHuman()) {
                // show card
                cardViewer.setCard(player.getDeck().get(0));
            }

            // buttons right
            final JPanel buttonsRightPanel = new JPanel();
            buttonsRightPanel.setLayout(new BoxLayout(buttonsRightPanel, BoxLayout.X_AXIS));
            buttonsRightPanel.setOpaque(false);
            buttonsRightPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            buttonsRightPanel.add(Box.createHorizontalStrut(SPACING));
            buttonsRightPanel.add(generateButtons[i]);
            rightPanel.add(buttonsRightPanel);

            // table of cards
            cardTables[i] = new CardTable(player.getDeck(), generateTitle(player.getDeck()), true);
            cardTables[i].addCardSelectionListener(cardViewer);
            cardTables[i].showCardCount(true);

            // add scrolling to right side
            final JScrollPane rightScrollPane = new JScrollPane(rightPanel);
            rightScrollPane.setBorder(FontsAndBorders.NO_BORDER);
            rightScrollPane.setOpaque(false);
            rightScrollPane.getViewport().setOpaque(false);
            rightScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            // contents of tab
            final JPanel tabPanel = new JPanel();
            final SpringLayout tabLayout = new SpringLayout();
            tabPanel.setLayout(tabLayout);
            tabPanel.add(cardTables[i]);
            tabPanel.add(rightScrollPane);

            tabLayout.putConstraint(SpringLayout.WEST, cardTables[i],
                                 SPACING, SpringLayout.WEST, tabPanel);
            tabLayout.putConstraint(SpringLayout.NORTH, cardTables[i],
                                 SPACING, SpringLayout.NORTH, tabPanel);
            tabLayout.putConstraint(SpringLayout.SOUTH, cardTables[i],
                                 -SPACING, SpringLayout.SOUTH, tabPanel);

            tabLayout.putConstraint(SpringLayout.EAST, cardTables[i],
                                 -SPACING, SpringLayout.WEST, rightScrollPane);

            tabLayout.putConstraint(SpringLayout.EAST, rightScrollPane,
                                 -SPACING, SpringLayout.EAST, tabPanel);
            tabLayout.putConstraint(SpringLayout.NORTH, rightScrollPane,
                                 SPACING, SpringLayout.NORTH, tabPanel);
            tabLayout.putConstraint(SpringLayout.SOUTH, rightScrollPane,
                                 -SPACING, SpringLayout.SOUTH, tabPanel);

            // add as a tab
            tabbedPane.addTab(null, tabPanel);
            final DuelConfig duelConfig = duel.getConfiguration();
            final PlayerProfile profile = duelConfig.getPlayerProfile(i);
            tabbedPane.setTabComponentAt(i, new PlayerPanel(profile));
        }

        tabbedPane.setPreferredSize(new Dimension(800, 0));

        // layout screen components.
        migLayout.setLayoutConstraints("insets 0, gap 0");
        setLayout(migLayout);
        add(leftScrollPane, "h 100%");
        add(tabbedPane, "h 100%, w 100%");
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
        for (int i = 0; i < statsViewers.length; i++) {
            final DuelPlayerConfig player = duel.getPlayers()[i];
            final MagicDeck deck = player.getDeck();
            cardTables[i].setCards(deck);
            cardTables[i].setTitle(generateTitle(deck));
            statsViewers[i].setDeck(deck);
            deckDescriptionViewers[i].setPlayer(player);
        }
        firePropertyChange(CP_DECK_CHANGED, true, false);
    }

    @SuppressWarnings("serial")
    private class PlayerPanel extends JPanel {

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

    }

}
