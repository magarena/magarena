package magic.ui.widget.card.decks;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.translate.MText;
import magic.ui.ScreenController;
import magic.ui.FontsAndBorders;
import magic.utility.DeckUtils;
import static magic.utility.DeckUtils.DECK_EXTENSION;
import static magic.utility.DeckUtils.getDeckFolder;
import magic.utility.MagicSystem;
import net.miginfocom.swing.MigLayout;

/**
 * Displays a list of decks containing a given card (see {@code setCard()}).
 */
@SuppressWarnings("serial")
public class CardDecksPanel extends JPanel {

    // translatable strings
    private static final String _S1 =  "Invalid Deck!";

    public static final String CP_DECKS_UPDATED = "6f0c65a7-6485-4468-9d62-31a505d307a9";
    private static final int TIMER_DELAY_MSECS = 500;

    private static SwingWorker<File[], Void> worker;
    private final JList<File> decksJList = new JList<>();
    private final MigLayout miglayout = new MigLayout();
    private final JScrollPane scroller = new JScrollPane();
    private MagicCardDefinition card;
    private Timer cooldownTimer;

    public CardDecksPanel() {

        decksJList.setOpaque(false);
        decksJList.setBackground(new Color(0, 0, 0, 1));
        decksJList.setForeground(Color.BLACK);
        decksJList.setFocusable(true);
        decksJList.setCellRenderer(new CardDecksListCellRenderer());
        decksJList.setFont(FontsAndBorders.FONT1);

        // scroll pane for deck names list
        scroller.setViewportView(decksJList);
        scroller.setBorder(null);
        scroller.setOpaque(false);
        scroller.getViewport().setOpaque(false);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        decksJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent evt) {
                if (isDoubleClick(evt)) {
                    showSelectedDeck(decksJList.locationToIndex(evt.getPoint()));
                }
            }
        });

        miglayout.setLayoutConstraints("insets 0, flowy");
        setLayout(miglayout);

        // these constraints fix decksButton in CardPanel not resizing correctly
        // when full screen and decksbutton is clicked to re-show image. Ensure you
        // test if these constraints are updated especially if you add an 'h' constraint.
        add(scroller, "w 100%, growy, pushy");

        cooldownTimer = new Timer(TIMER_DELAY_MSECS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cooldownTimer.stop();
                showDecksContainingCard(CardDecksPanel.this.card);
            }
        });

    }

    private void showSelectedDeck(int row) {
        if (row >= 0) {
            final File deckFile = decksJList.getModel().getElementAt(row);
            final MagicDeck deck = DeckUtils.loadDeckFromFile(deckFile.toPath());
            if (deck.isValid()) {
                ScreenController.showDeckScreen(deck, card);
            } else {
                ScreenController.showWarningMessage(String.format("<html><b>%s</b><br>%s</html>",
                        MText.get(_S1),
                        deck.getDescription().replace("\n", "<br>"))
                );
            }
        }
    }

    private boolean isDoubleClick(MouseEvent evt) {
        return evt.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(evt);
    }

    public void setCard(MagicCardDefinition aCard) {

        if (worker != null && worker.isDone() == false && worker.isCancelled() == false) {
            worker.cancel(true);
        }

        this.card = aCard;

        decksJList.setListData(new File[]{});
        cooldownTimer.setInitialDelay(TIMER_DELAY_MSECS);
        cooldownTimer.restart();
    }

    private void showDecksContainingCard(final MagicCardDefinition card) {

        worker = new SwingWorker<File[], Void>() {

            @Override
            protected File[] doInBackground() throws Exception {
                final List<File> deckFiles = getDecksContainingCard(card);
                sortDecksByFilename(deckFiles);
                return deckFiles.toArray(new File[0]);
            }

            @Override
            protected void done() {
                try {
                    decksJList.setListData(get());
                    fireDecksUpdatedEvent();
                } catch (ExecutionException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    System.err.println("CardDecksPanel.SwingWorker.InterruptedException");
                } catch (CancellationException ex) {
                    if (MagicSystem.isDevMode()) {
                        System.err.println("CardDecksPanel.SwingWorker.CancellationException");
                    }
                }
            }

            private List<File> getDecksContainingCard(final MagicCardDefinition cardDef) {
                final List<File> matchingDeckFiles = new ArrayList<>();
                if (cardDef != null && !isCancelled()) {
                    final List<File> allDeckFiles = new ArrayList<>();
                    retrieveDeckFiles(new File(getDeckFolder()), allDeckFiles);
                    for (File deckFile : allDeckFiles) {
                        if (isCancelled()) {
                            break;
                        }
                        try (final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(deckFile), "UTF-8"))) {
                            String line;
                            while ((line = br.readLine()) != null && !isCancelled()) {
                                if (!line.startsWith("#") & !line.startsWith(">") & !line.trim().isEmpty()) {
                                    if (line.substring(line.indexOf(" ")).trim().equals(cardDef.getName())) {
                                        matchingDeckFiles.add(deckFile);
                                        break;
                                    }
                                }
                            }
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                }
                return matchingDeckFiles;
            }

            private void retrieveDeckFiles(final File folder, final List<File> deckFiles) {
                final File[] files = folder.listFiles();
                for (final File file : files) {
                    if (isCancelled()) {
                        break;
                    }
                    if (file.isDirectory()) {
                        retrieveDeckFiles(file, deckFiles);
                    } else if (file.getName().endsWith(DECK_EXTENSION)) {
                        deckFiles.add(file);
                    }
                }
            }

            /**
             * In Windows, DirectoryStream returns a list of files already sorted by filename. In Linux it does not, so need to specifically sort list.
             */
            private void sortDecksByFilename(final List<File> files) {
                if (isCancelled() == false) {
                    Collections.sort(files, new Comparator<File>() {
                        @Override
                        public int compare(File o1, File o2) {
                            return o1.getAbsolutePath().compareTo(o2.getAbsolutePath());
                        }
                    });
                }
            }

        };
        worker.execute();
    }

    private void fireDecksUpdatedEvent() {
        firePropertyChange(CP_DECKS_UPDATED, -1, decksJList.getModel().getSize());
    }

    int getDecksCount() {
        return decksJList.getModel().getSize();
    }

}
