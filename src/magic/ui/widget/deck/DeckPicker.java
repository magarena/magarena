package magic.ui.widget.deck;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import magic.data.DeckType;
import magic.data.stats.MagicStats;
import magic.exception.InvalidDeckException;
import magic.firemind.FiremindJsonReader;
import magic.model.MagicDeck;
import magic.translate.MText;
import magic.ui.FontsAndBorders;
import magic.ui.dialog.DecksFilterDialog;
import magic.ui.screen.interfaces.IDeckConsumer;
import magic.ui.widget.duel.viewer.CardViewer;
import magic.utility.DeckUtils;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DeckPicker extends JPanel {

    // translatable strings
    private static final String _S1 = "All Decks (%d)";
    private static final String _S2 = "Filtered Decks (%d)";

    // ui components
    private final MigLayout migLayout = new MigLayout();
    private final JComboBox<DeckType> deckTypeJCombo = new JComboBox<>();
    private final JList<MagicDeck> decksJList = new JList<>();
    private final JScrollPane scroller = new JScrollPane();
    private final FilterPanel filterPanel = new FilterPanel();

    // properties
    private DeckType selectedDeckType = DeckType.Preconstructed;
    private final List<IDeckConsumer> listeners = new ArrayList<>();
    private ListSelectionListener listSelectionListener;
    private DeckFilter deckFilter = null;

    public DeckPicker() {
        setLookAndFeel();
        refreshLayout();
        setListeners();
        refreshContent();
    }

    public void addListener(final IDeckConsumer listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    private void setLookAndFeel() {
        setOpaque(false);
        setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);
        setLayout(migLayout);
        setMaximumSize(CardViewer.getSidebarImageSize());
        setPreferredSize(CardViewer.getSidebarImageSize());
        // deck types combo
        deckTypeJCombo.setLightWeightPopupEnabled(false);
        deckTypeJCombo.setFocusable(false);
        deckTypeJCombo.setFont(FontsAndBorders.FONT2);
        // decks list
        decksJList.setOpaque(false);
        decksJList.setBackground(new Color(0, 0, 0, 1));
        decksJList.setForeground(Color.BLACK);
        decksJList.setFocusable(true);
        decksJList.setCellRenderer(new DecksListCellRenderer());
        // scroll pane for deck names list
        scroller.setViewportView(decksJList);
        scroller.setBorder(null);
        scroller.setOpaque(false);
        scroller.getViewport().setOpaque(false);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    }

    private void refreshLayout() {
        migLayout.setLayoutConstraints("insets 0, gap 0, flowy");
        migLayout.setColumnConstraints("[fill, grow]");
        migLayout.setRowConstraints("[][fill, grow]");
        add(getDeckFilterPanel());
        add(scroller);
    }

    private JPanel getDeckFilterPanel() {
        final JPanel panel = new JPanel(new MigLayout("insets 4, gap 0, flowy"));
        panel.setOpaque(false);
        panel.add(deckTypeJCombo, "w 100%, h 30!");
        panel.add(filterPanel, "w 100%");
        return panel;
    }

    private void setListeners() {
        // deck types combo
        deckTypeJCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                // ensures dropdown list closes on click *before* refreshing decks list.
                SwingUtilities.invokeLater(() -> {
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    selectedDeckType = (DeckType) e.getItem();
                    refreshDecksList();
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                });
            }
        });
        // deck names list : need to be able to add/remove to prevent multiple events.
        listSelectionListener = getListSelectionListener();
    }



    final public void refreshContent() {
        // deck types combo
        final DeckType deckTypes[] = DeckType.getPredefinedDecks().toArray(new DeckType[0]);
        deckTypeJCombo.setModel(new DefaultComboBoxModel<>(deckTypes));
        deckTypeJCombo.setSelectedIndex(0);
        refreshDecksList();
    }

    private void refreshDecksList() {
        // ignore list selection events while setting list data.
        decksJList.removeListSelectionListener(listSelectionListener);
        decksJList.setListData(getDecksListData());
        decksJList.addListSelectionListener(listSelectionListener);
        if (decksJList.getModel().getSize() > 0) {
            decksJList.setSelectedIndex(0);
            filterPanel.setDecksCount(decksJList.getModel().getSize());
        } else {
            filterPanel.setDecksCount(0);
            for (IDeckConsumer listener : listeners) {
                listener.setDeck(new MagicDeck());
            }
        }
    }

    private MagicDeck[] getPopularDecks() {
       return MagicStats.getMostPlayedDecks().toArray(new MagicDeck[0]);
    }

    private MagicDeck[] getWinningDecks() {
       return MagicStats.getTopWinningDecks().toArray(new MagicDeck[0]);
    }

    private MagicDeck[] getRecentDecks() {
       return MagicStats.getRecentlyPlayedDecks().toArray(new MagicDeck[0]);
    }

    private MagicDeck[] getDecksListData() {
        switch (selectedDeckType) {
            case Preconstructed:
                return getFilteredDecksListData(DeckUtils.getPrebuiltDecksFolder());
            case Custom:
                return getFilteredDecksListData(DeckUtils.getDecksFolder());
            case Firemind:
                FiremindJsonReader.refreshTopDecks();
                return getFilteredDecksListData(DeckUtils.getFiremindDecksFolder());
            case PopularDecks:
                return getPopularDecks();
            case WinningDecks:
                return getWinningDecks();
            case RecentDecks:
                return getRecentDecks();
            default:
                return new MagicDeck[0];
        }
    }

    private MagicDeck[] getFilteredDecksListData(final Path decksPath) {
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(decksPath, "*.{dec}")) {
            final List<MagicDeck> decks = loadDecks(ds);
            sortDecksByFilename(decks);
            return decks.toArray(new MagicDeck[0]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * In Windows, DirectoryStream returns a list of files already sorted by
     * filename. In Linux it does not, so need to specifically sort list.
     */
    private void sortDecksByFilename(final List<MagicDeck> decks) {
        decks.sort((o1, o2) -> o1.getFilename().compareToIgnoreCase(o2.getFilename()));
    }

    private List<MagicDeck> loadDecks(final DirectoryStream<Path> ds) {
        final List<MagicDeck> decks = new ArrayList<>();
        for (final Path filePath : ds) {
            final MagicDeck deck = loadDeck(filePath);
            if (isValidFilteredDeck(deck)) {
                decks.add(deck);
            }
        }
        return decks;
    }

    private MagicDeck loadDeck(final Path deckFilePath) {
        try {
            return DeckUtils.loadDeckFromFile(deckFilePath);
        } catch (InvalidDeckException ex) {
            // Instead of prompting user with an error dialog for each
            // invalid deck found, create an empty deck flagged as invalid
            // with its description set to the error message. Invalid decks
            // will have their names scored out in the decks list.
            final MagicDeck deck = new MagicDeck();
            deck.setFilename(deckFilePath.getFileName().toString());
            deck.setInvalidDeck(ex.toString());
            return deck;
        }
    }

    private boolean isValidFilteredDeck(final MagicDeck deck) {
        if (deckFilter != null) {
            return deckFilter.isDeckValid(deck);
        } else {
            return true;
        }
    }

    private ListSelectionListener getListSelectionListener() {
        return new ListSelectionListener() {
            @Override
            public void valueChanged(final ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    SwingUtilities.invokeLater(() -> {
                        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        final MagicDeck deck = decksJList.getSelectedValue();
                        for (IDeckConsumer listener : listeners) {
                            switch (selectedDeckType) {
                                case Random:
                                    listener.setDeck(deck.getName(), selectedDeckType);
                                    break;
                                case PopularDecks:
                                    listener.setDeck(deck);
                                    break;
                                case WinningDecks:
                                    listener.setDeck(deck);
                                    break;
                                case RecentDecks:
                                    listener.setDeck(deck);
                                    break;
                                default:
                                    listener.setDeck(deck, getDeckPath(deck.getName(), selectedDeckType));
                                    break;
                            }
                        }
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    });
                }
            }
            private Path getDeckPath(final String deckName, final DeckType deckType) {
                switch (deckType) {
                    case Preconstructed:
                        return DeckUtils.getPrebuiltDecksFolder().resolve(deckName + ".dec");
                    case Custom:
                        return DeckUtils.getDecksFolder().resolve(deckName + ".dec");
                    case Firemind:
                        return DeckUtils.getDecksFolder().resolve("firemind").resolve(deckName + ".dec");
                    default:
                        throw new RuntimeException("getDeckPath() not implemented for decktype: " + deckType);
                }
            }
        };
    }

    private class FilterPanel extends JPanel {

        // ui components
        private final MigLayout migLayout = new MigLayout();
        private final JButton filterButton = new JButton();

        public FilterPanel() {
            setLookAndFeel();
            refreshLayout();
            setListeners();
//            refreshContent();
        }

        private void setLookAndFeel() {
            setOpaque(false);
            setLayout(migLayout);
            // filter button
            filterButton.setFont(FontsAndBorders.FONT1);
            filterButton.setFocusable(false);
            filterButton.setHorizontalAlignment(SwingConstants.LEFT);
        }

        private void refreshLayout() {
            removeAll();
            migLayout.setLayoutConstraints("insets 0");
            add(filterButton, "w 100%");
        }

        private void setListeners() {
            // deck types combo
            filterButton.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    final DecksFilterDialog dialog = new DecksFilterDialog();
                    dialog.setVisible(true);
                    if (!dialog.isCancelled()) {
                        deckFilter = dialog.getDeckFilter();
                        refreshDecksList();
                    }
                }
            });
        }

        public void setDecksCount(final int deckCount) {
            filterButton.setText(MText.get(deckFilter == null ? _S1 : _S2, deckCount));
        }

    }

}
