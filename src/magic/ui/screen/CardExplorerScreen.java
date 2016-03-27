package magic.ui.screen;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JPanel;

import magic.data.CardDefinitions;
import magic.data.MagicIcon;
import magic.data.MagicSetDefinitions;
import magic.translate.UiString;
import magic.ui.MagicFrame;
import magic.ui.MagicImages;
import magic.ui.MagicLogs;
import magic.ui.ScreenOptionsOverlay;
import magic.ui.explorer.ExplorerPanel;
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
import magic.utility.WikiPage;

@SuppressWarnings("serial")
public class CardExplorerScreen
    extends AbstractScreen
    implements IStatusBar, IActionBar, IOptionsMenu, IWikiPage {

    // translatable strings
    private static final String _S1 = "Card Explorer";
    private static final String _S2 = "Close";
    private static final String _S3 = "View Script";
    private static final String _S4 = "View the script and groovy files for the selected card.<br>(or double-click row)";

    private final ExplorerPanel content;

    public CardExplorerScreen() {
        content = new ExplorerPanel();
        setContent(content);
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
                MagicImages.getIcon(MagicIcon.EDIT_ICON),
                UiString.get(_S3), UiString.get(_S4),
                new AbstractAction() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        content.showCardScriptScreen();
                    }
                }
            )
        );
        if (MagicSystem.isDevMode() || MagicSystem.isDebugMode()) {
            buttons.add(new ActionBarButton(
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
                )
            );
            buttons.add(new ActionBarButton(
                    MagicImages.getIcon(MagicIcon.STATS_ICON),
                    "Save Statistics [DevMode Only]", "Creates CardStatistics.txt to view current card completion.",
                    new AbstractAction() {
                        @Override
                        public void actionPerformed(final ActionEvent e) {
                            try {
                                saveCardStatistics();
                            } catch (IOException e1) {
                                throw new RuntimeException(e1);
                            }
                        }
                    }
                )
            );
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

    private void saveCardStatistics() throws IOException {
        ArrayList<String> allSetInfo = new ArrayList<String>();
        allSetInfo.add("Set,Cards,Playable,Unimplemented");
        for (int i = 0; i < content.filterPanel.setsCheckBoxes.length; i++) {
            content.filterPanel.resetFilters();
            content.filterPanel.setsCheckBoxes[i].setSelected(true);
            content.updateCardPool();
            String setText = content.filterPanel.setsCheckBoxes[i].getText() + "      " + content.generatePoolTitle();
            allSetInfo.add(setText.replaceAll("      ",",").replaceAll(" \\(\\d*\\.\\d%\\)","").replaceAll("Cards: ","").replaceAll("Playable: ","").replaceAll("Unimplemented: ",""));
        }
        final Path savePath = MagicFileSystem.getDataPath(DataPath.LOGS).resolve("CardStatistics.csv");
        try (final PrintWriter writer = new PrintWriter(savePath.toFile())) {
            allSetInfo.forEach(writer::println);
        }
        Desktop.getDesktop().open(MagicFileSystem.getDataPath(DataPath.LOGS).toFile());
        content.filterPanel.resetFilters();
        content.updateCardPool();
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
    public String getWikiPageName() {
        return WikiPage.CARDS_EXPLORER;
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
        return null;
    }

}
