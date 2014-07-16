package magic.ui.widget.deck;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
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
import magic.MagicMain;
import magic.data.DeckType;
import magic.data.DeckUtils;
import magic.model.MagicDeck;
import magic.ui.dialog.DecksFilterDialog;
import magic.ui.screen.interfaces.IDeckConsumer;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.io.FilenameUtils;

@SuppressWarnings("serial")
public class DeckPicker extends JPanel {

    private static final Theme THEME = ThemeFactory.getInstance().getCurrentTheme();
    private static final Color HIGHLIGHT_BACK = THEME.getColor(Theme.COLOR_TITLE_BACKGROUND);
    private static final Color HIGHLIGHT_FORE = THEME.getColor(Theme.COLOR_TITLE_FOREGROUND);

    // ui components
    private final MigLayout migLayout = new MigLayout();
    private final JComboBox<DeckType> deckTypeJCombo = new JComboBox<>();
    private final JList<String> deckNamesJList = new JList<>();
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
        // deck types combo
        deckTypeJCombo.setLightWeightPopupEnabled(false);
        deckTypeJCombo.setFocusable(false);
        deckTypeJCombo.setFont(FontsAndBorders.FONT2);
        // deck names list
        deckNamesJList.setOpaque(false);
        deckNamesJList.setBackground(new Color(0, 0, 0, 1));
        deckNamesJList.setForeground(Color.BLACK);
        deckNamesJList.setSelectionBackground(HIGHLIGHT_BACK);
        deckNamesJList.setSelectionForeground(HIGHLIGHT_FORE);
        deckNamesJList.setFocusable(true);
        // scroll pane for deck names list
        scroller.setViewportView(deckNamesJList);
        scroller.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.DARK_GRAY));
        scroller.setOpaque(false);
        scroller.getViewport().setOpaque(false);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("insets 0, gap 0, flowy");
        add(getDeckFilterPanel(), "w 100%");
        add(scroller, "w 100%, h 100%");
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
        deckTypeJCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // ensures dropdown list closes on click *before* refreshing decks list.
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                            selectedDeckType = (DeckType) e.getItem();
                            refreshDecksList();
                            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        }
                    });
                }
            }
        });
        // deck names list : need to be able to add/remove to prevent multiple events.
        listSelectionListener = getListSelectionListener();
    }
    


    final public void refreshContent() {
        // deck types combo
        final DeckType deckTypes[] = DeckType.PREDEFINED_DECKS.toArray(new DeckType[DeckType.PREDEFINED_DECKS.size()]);
        deckTypeJCombo.setModel(new DefaultComboBoxModel<>(deckTypes));
        deckTypeJCombo.setSelectedIndex(0);
        // deck names list
        refreshDecksList();
    }

    private void refreshDecksList() {
        // ignore list selection events while setting list data.
        deckNamesJList.removeListSelectionListener(listSelectionListener);
        deckNamesJList.setListData(getDecksListData());
        deckNamesJList.addListSelectionListener(listSelectionListener);
        if (deckNamesJList.getModel().getSize() > 0) {
            deckNamesJList.setSelectedIndex(0);
            filterPanel.setDecksCount(deckNamesJList.getModel().getSize());
        } else {
            filterPanel.setDecksCount(0);
            for (IDeckConsumer listener : listeners) {
                listener.setDeck(new MagicDeck(), null);
            }                        
        }
    }

    private String[] getDecksListData() {
        switch (selectedDeckType) {
            case Preconstructed:
                return getFilteredDecksListData(DeckUtils.getPrebuiltDecksFolder());
            case Custom:
                return getFilteredDecksListData(Paths.get(DeckUtils.getDeckFolder()));
            default:
                return new String[0];
            }
    }

    private String[] getFilteredDecksListData(final Path decksPath) {
        final List<String> deckNames = new ArrayList<>();
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(decksPath, "*.{dec}")) {
            for (Path path : ds) {
                final MagicDeck deck = DeckUtils.loadDeckFromFile(path.toString());
                if (isValidFilteredDeck(deck)) {
                    deckNames.add(deck.getName());
                }
            }
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
        return deckNames.toArray(new String[deckNames.size()]);
    }

    private boolean isValidFilteredDeck(final MagicDeck deck) {
        if (deckFilter != null) {
            return deckFilter.isDeckValid(deck);
        } else {
            return (deck != null);
        }
    }

    private String[] getDeckFilenames(final Path decksPath) {
        final List<String> deckNames = new ArrayList<>();
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(decksPath, "*.{dec}")) {
            for (Path path : ds) {
                deckNames.add(FilenameUtils.getBaseName(path.toString()));
            }
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
        return deckNames.toArray(new String[deckNames.size()]);
    }

    private ListSelectionListener getListSelectionListener() {
        return new ListSelectionListener() {
            @Override
            public void valueChanged(final ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                            final String deckName = deckNamesJList.getSelectedValue();
                            for (IDeckConsumer listener : listeners) {
                                if (selectedDeckType == DeckType.Random) {
                                    listener.setDeck(deckName, selectedDeckType);
                                } else {
                                    listener.setDeck(getDeck(deckName, selectedDeckType), getDeckPath(deckName, selectedDeckType));
                                }
                            }
                            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        }
                    });
                }
            }

            private Path getDeckPath(final String deckName, final DeckType deckType) {
                switch (deckType) {
                    case Preconstructed:
                        return DeckUtils.getPrebuiltDecksFolder().resolve(deckName + ".dec");
                    case Custom:
                        return Paths.get(DeckUtils.getDeckFolder()).resolve(deckName + ".dec");
                    default:
                        throw new RuntimeException("getDeckPath() not implemented for decktype: " + deckType);
                }
            }

            private MagicDeck getDeck(final String deckName, final DeckType deckType) {
                return DeckUtils.loadDeckFromFile(getDeckPath(deckName, deckType).toString());
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
//            add(decksListHeaderLabel, "w 100%");
        }

        private void setListeners() {
            // deck types combo
            filterButton.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    final DecksFilterDialog dialog = new DecksFilterDialog(MagicMain.rootFrame);
                    dialog.setVisible(true);
                    if (!dialog.isCancelled()) {
                        deckFilter = dialog.getDeckFilter();
                        refreshDecksList();
                    }
                }
            });
        }

        public void setDecksCount(final int deckCount) {
            if (deckFilter == null) {
                filterButton.setText("All Decks (" + deckCount + ")");
            } else {
                filterButton.setText("Filtered Decks (" + deckCount + ")");
            }
        }

    }

}
