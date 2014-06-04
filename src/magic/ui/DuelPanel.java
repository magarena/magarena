package magic.ui;

import magic.data.CardImagesProvider;
import magic.data.DuelConfig;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.model.MagicDuel;
import magic.model.MagicPlayerDefinition;
import magic.model.player.HumanPlayer;
import magic.model.player.PlayerProfile;
import magic.ui.viewer.CardViewer;
import magic.ui.viewer.DeckDescriptionViewer;
import magic.ui.viewer.DeckStatisticsViewer;
import magic.ui.viewer.DeckStrengthViewer;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.player.PlayerDetailsPanel;
import net.miginfocom.swing.MigLayout;

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

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DuelPanel extends TexturedPanel {

    private static final long serialVersionUID = 1L;

    private static final int SPACING = 10;
    private static final String GENERATE_BUTTON_TEXT = "Generate Deck";

    private final MagicDuel duel;
    private final JTabbedPane tabbedPane;
    private final DeckStrengthViewer strengthViewer;
    private final DeckDescriptionViewer[] deckDescriptionViewers;
    private final CardViewer cardViewer;
    private final CardTable[] cardTables;
    private final JButton[] generateButtons;
    private final DeckStatisticsViewer[] statsViewers;

    public DuelPanel(final MagicDuel duel) {

        this.duel=duel;

        setBackground(FontsAndBorders.MAGSCREEN_FADE_COLOR);
        final SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);

        // buttons
        final JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.setOpaque(false);

        add(buttonsPanel);

        // left top
        final JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        // card image
        cardViewer=new CardViewer(false);
        cardViewer.setPreferredSize(CardImagesProvider.CARD_DIMENSION);
        cardViewer.setMaximumSize(CardImagesProvider.CARD_DIMENSION);
        cardViewer.setCard(MagicCardDefinition.UNKNOWN,0);
        cardViewer.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(cardViewer);

        leftPanel.add(Box.createVerticalStrut(SPACING));

        // add scrolling to left side
        final JScrollPane leftScrollPane = new JScrollPane(leftPanel);
        leftScrollPane.setBorder(FontsAndBorders.NO_BORDER);
        leftScrollPane.setOpaque(false);
        leftScrollPane.getViewport().setOpaque(false);
        leftScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(leftScrollPane);

        // create tabs for each player
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);

        final MagicPlayerDefinition[] players = duel.getPlayers();
        cardTables = new CardTable[players.length];
        deckDescriptionViewers = new DeckDescriptionViewer[players.length];
        statsViewers = new DeckStatisticsViewer[players.length];
        generateButtons = new JButton[players.length];

        // deck strength tester
        strengthViewer=new DeckStrengthViewer(duel);
        strengthViewer.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (int i = 0; i < players.length; i++) {

            final MagicPlayerDefinition player = players[i];
            player.setAvatar(duel.getConfiguration().getPlayerProfile(i).getAvatar());

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
            generateButtons[i].setEnabled(duel.getGamesPlayed() == 0);
            generateButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent event) {
                    duel.buildDeck(player);
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

            if (!player.isArtificial()) {
                rightPanel.add(strengthViewer);
                rightPanel.add(Box.createVerticalStrut(SPACING));

                // show card
                cardViewer.setCard(player.getDeck().get(0),0);
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
            cardTables[i] = new CardTable(player.getDeck(), cardViewer, generateTitle(player.getDeck()), true);

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

        add(tabbedPane);

        // set sizes by defining gaps between components
        final Container contentPane = this;

        // left side's gap (left top)
        springLayout.putConstraint(SpringLayout.NORTH, leftScrollPane,
                             SPACING, SpringLayout.NORTH, contentPane);
        springLayout.putConstraint(SpringLayout.WEST, leftScrollPane,
                             SPACING, SpringLayout.WEST, contentPane);

        // left side's gap with tabbed pane
        springLayout.putConstraint(SpringLayout.WEST, tabbedPane,
                             SPACING, SpringLayout.EAST, leftScrollPane);

        // tabbed pane's gap (top right bottom)
        springLayout.putConstraint(SpringLayout.NORTH, tabbedPane,
                             0, SpringLayout.NORTH, leftPanel);
        springLayout.putConstraint(SpringLayout.EAST, tabbedPane,
                             -SPACING, SpringLayout.EAST, contentPane);
        springLayout.putConstraint(SpringLayout.SOUTH, tabbedPane,
                             -SPACING, SpringLayout.SOUTH, contentPane);

        // buttons' gap (top left right bottom)
        springLayout.putConstraint(SpringLayout.SOUTH, leftScrollPane,
                             -SPACING, SpringLayout.NORTH, buttonsPanel);
        springLayout.putConstraint(SpringLayout.EAST, buttonsPanel,
                             SPACING, SpringLayout.WEST, tabbedPane);
        springLayout.putConstraint(SpringLayout.SOUTH, buttonsPanel,
                             -SPACING, SpringLayout.SOUTH, contentPane);
        springLayout.putConstraint(SpringLayout.WEST, buttonsPanel,
                             SPACING, SpringLayout.WEST, contentPane);

    }

    String generateTitle(final MagicDeck deck) {
        return "Deck (" + deck.getName() + ") - " + deck.size() + " cards";
    }

    public MagicDuel getDuel() {
        return duel;
    }

    public MagicPlayerDefinition getSelectedPlayer() {
        return duel.getPlayers()[tabbedPane.getSelectedIndex()];
    }

    public void setSelectedTab(final int tab) {
        tabbedPane.setSelectedIndex(tab);
    }

    public void updateDecksAfterEdit() {
        for (int i = 0; i < statsViewers.length; i++) {
            cardTables[i].setCards(duel.getPlayers()[i].getDeck());
            cardTables[i].setTitle(generateTitle(duel.getPlayers()[i].getDeck()));
            statsViewers[i].setDeck(duel.getPlayers()[i].getDeck());
            deckDescriptionViewers[i].setPlayer(duel.getPlayers()[i]);
        }
    }

    public void haltStrengthViewer() {
        strengthViewer.halt();
    }

    @SuppressWarnings("serial")
    private class PlayerPanel extends JPanel {

        public PlayerPanel(final PlayerProfile profile) {
            setLayout(new MigLayout("insets 0"));
            setOpaque(false);
            add(new JLabel(profile.getAvatar().getIcon(4)));
            add(new PlayerDetailsPanel(profile, Color.BLACK), "w 100%");
            add(getScoreLabel(getScore(profile)), "w 100%");
            setPreferredSize(new Dimension(250, 54));
        }

        private int getScore(final PlayerProfile profile) {
            if (profile instanceof HumanPlayer) {
                return duel.getGamesWon();
            } else {
                return duel.getGamesPlayed() - duel.getGamesWon();
            }
        }

        private JLabel getScoreLabel(final int score) {
            final JLabel lbl = new JLabel(Integer.toString(score));
            lbl.setFont(new Font("Dialog", Font.PLAIN, 24));
            lbl.setHorizontalAlignment(SwingConstants.RIGHT);
            return lbl;
        }

    }

}
