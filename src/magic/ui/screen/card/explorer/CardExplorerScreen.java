package magic.ui.screen.card.explorer;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import magic.data.CardDefinitions;
import magic.data.MagicIcon;
import magic.data.MagicSetDefinitions;
import magic.translate.StringContext;
import magic.translate.UiString;
import magic.ui.MagicFrame;
import magic.ui.MagicImages;
import magic.ui.MagicLogs;
import magic.ui.ScreenOptionsOverlay;
import magic.ui.screen.AbstractScreen;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IOptionsMenu;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.interfaces.IWikiPage;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.MenuButton;
import magic.ui.screen.widget.MenuPanel;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;
import magic.utility.MagicSystem;
import magic.ui.WikiPage;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class CardExplorerScreen
    extends AbstractScreen
    implements IStatusBar, IActionBar, IOptionsMenu, IWikiPage {

    // translatable strings
    private static final String _S1 = "Card Explorer";
    private static final String _S2 = "Close";
    private static final String _S3 = "View Script";
    private static final String _S4 = "View the script and groovy files for the selected card.<br>(or double-click row)";
    private static final String _S5 = "Lucky Dip";
    private static final String _S6 = "Selects a random card from the list of cards displayed.";
    private static final String _S7 = "Change layout";
    private static final String _S8 = "Cycles through a number of different screen layouts.";
    @StringContext(eg = "Total number of cards displayed in explorer.")
    private static final String _S9 = "%s cards";
    private static final String _S10 = "Playable: %s   •   Unimplemented: %s";

    private final ExplorerPanel contentPanel;
    private final StatusPanel statusPanel;

    public CardExplorerScreen() {
        statusPanel = new StatusPanel();
        contentPanel = new ExplorerPanel(this);
        setContent(contentPanel);
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

    private MenuButton getCardScriptButton() {
        return new ActionBarButton(
                MagicImages.getIcon(MagicIcon.EDIT_ICON),
                UiString.get(_S3), UiString.get(_S4),
                new AbstractAction() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        contentPanel.showCardScriptScreen();
                    }
                }
        );
    }

    private MenuButton getRandomCardButton() {
        return new ActionBarButton(
                MagicImages.getIcon(MagicIcon.RANDOM_ICON),
                UiString.get(_S5), UiString.get(_S6),
                new AbstractAction() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        contentPanel.selectRandomCard();
                    }
                }
        );
    }

    private MenuButton getMissingCardsButton() {
        return new ActionBarButton(
                MagicImages.getIcon(MagicIcon.SAVE_ICON),
                "Save Missing Cards [DevMode Only]", "Creates CardsMissingInMagarena.txt which can be used by the Scripts Builder.",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        try {
                            saveMissingCardsList();
                        } catch (IOException e1) {
                            throw new RuntimeException(e1);
                        }
                    }
                }
        );
    }

    private MenuButton getStatsButton() {
        return new ActionBarButton(
                MagicImages.getIcon(MagicIcon.STATS_ICON),
                "Save Statistics [DevMode Only]", "Creates CardStatistics.txt to view current card completion.",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        try {
                            MagicSetDefinitions.createSetStatistics();
                        } catch (IOException e1) {
                            throw new RuntimeException(e1);
                        }
                    }
                }
        );
    }

    private MenuButton getLayoutButton() {
        return new ActionBarButton(
                MagicImages.getIcon(MagicIcon.LAYOUT_ICON),
                UiString.get(_S7), UiString.get(_S8),
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ExplorerScreenLayout.setNextLayout();
                        contentPanel.refreshLayout();
                    }
                }
        );
    }

    @Override
    public List<MenuButton> getMiddleActions() {
        final List<MenuButton> buttons = new ArrayList<>();
        buttons.add(getCardScriptButton());
        buttons.add(getRandomCardButton());
        buttons.add(getLayoutButton());
        if (MagicSystem.isDevMode()) {
            buttons.add(getMissingCardsButton());
            buttons.add(getStatsButton());
        }
        return buttons;
    }

    private void saveMissingCardsList() throws IOException {
        final List<String> missingCards = CardDefinitions.getMissingCardNames();
        Collections.sort(missingCards);
        final Path savePath = MagicFileSystem.getDataPath(DataPath.LOGS).resolve("CardsMissingInMagarena.txt");
        try (final PrintWriter writer = new PrintWriter(savePath.toFile())) {
            missingCards.forEach(writer::println);
        }
        Desktop.getDesktop().open(MagicFileSystem.getDataPath(DataPath.LOGS).toFile());
    }

    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        MagicSetDefinitions.clearLoadedSets();
        MagicLogs.clearLoadedLogs();
        return true;
    }

    @Override
    public void showOptionsMenuOverlay() {
        new ScreenOptions(getFrame());
    }

    @Override
    public WikiPage getWikiPageName() {
        return WikiPage.CARDS_EXPLORER;
    }

    public void refreshTotals(int total, int playable, int missing) {
        statusPanel.refreshTotals(total, playable, missing);
    }

    private class ScreenOptions extends ScreenOptionsOverlay {

        public ScreenOptions(final MagicFrame frame) {
            super(frame);
        }

        @Override
        protected MenuPanel getScreenMenu() {
            return null;
        }

        @Override
        protected boolean showPreferencesOption() {
            return false;
        }

    }

    @Override
    public JPanel getStatusPanel() {
        return statusPanel;
    }

    private final class StatusPanel extends JPanel {

        private final JLabel totals1;
        private final JLabel totals2;

        StatusPanel() {
            setOpaque(false);
            totals1 = createLabel(16);
            totals2 = createLabel(14);
            setLayout(new MigLayout("insets 0, gap 2, flowy"));
            add(totals1, "w 100%");
            add(totals2, "w 100%");
        }

        private JLabel createLabel(int fontSize) {
            final JLabel lbl = new JLabel();
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Dialog", Font.PLAIN, fontSize));
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            return lbl;
        }

        private String getCountCaption(final int total, final int value) {
            final double percent = value / (double)total * 100;
            DecimalFormat df = new DecimalFormat("0.0");
            return NumberFormat.getInstance().format(value) + " (" + (!Double.isNaN(percent) ? df.format(percent) : "0.0") + "%)";
        }

        void refreshTotals(int total, int playable, int missing) {
            totals1.setText(UiString.get(_S9,
                NumberFormat.getInstance().format(total))
            );
            totals2.setText(UiString.get(_S10,
                    getCountCaption(total, playable),
                    getCountCaption(total, missing))
            );
        }
    }

}
