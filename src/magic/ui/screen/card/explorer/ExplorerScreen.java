package magic.ui.screen.card.explorer;

import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import magic.data.CardDefinitions;
import magic.data.MagicIcon;
import magic.data.MagicSetDefinitions;
import magic.translate.UiString;
import magic.ui.MagicLogs;
import magic.ui.WikiPage;
import magic.ui.screen.widget.MenuButton;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;
import magic.ui.screen.HeaderFooterScreen;
import magic.utility.MagicSystem;

@SuppressWarnings("serial")
public class ExplorerScreen extends HeaderFooterScreen {

    // translatable strings
    private static final String _S1 = "Card Explorer";
    private static final String _S3 = "View Script";
    private static final String _S4 = "View the script and groovy files for the selected card.<br>(or double-click row)";
    private static final String _S5 = "Lucky Dip";
    private static final String _S6 = "Selects a random card from the list of cards displayed.";
    private static final String _S7 = "Change layout";
    private static final String _S8 = "Cycles through a number of different screen layouts.";

    private ExplorerContentPanel contentPanel;
    private ExplorerHeaderPanel headerPanel;

    public ExplorerScreen() {
        super(UiString.get(_S1));
        useLoadingScreen(this::initUI);
    }

    @Override
    protected boolean isCardDataRequired() {
        return true;
    }

    private void initUI() {
        headerPanel = new ExplorerHeaderPanel();
        contentPanel = new ExplorerContentPanel(this);
        setHeaderContent(headerPanel);
        setMainContent(contentPanel);
        setFooterButtons();
        setWikiPage(WikiPage.CARDS_EXPLORER);
    }

    private void setFooterButtons() {
        addToFooter(
                MenuButton.build(this::doShowScriptScreen, MagicIcon.EDIT, _S3, _S4),
                MenuButton.build(this::doSelectRandomCard, MagicIcon.RANDOM, _S5, _S6),
                MenuButton.build(this::doSwitchLayout, MagicIcon.LAYOUT, _S7, _S8)
        );
        if (MagicSystem.isDevMode()) {
            addToFooter(
                    MenuButton.build(this::doSaveMissingCardsFile,
                            MagicIcon.SAVE,
                            "[DevMode] Save missing cards file",
                            "Creates CardsMissingInMagarena.txt for use with the Scripts Builder."
                    ),
                    MenuButton.build(this::doCreateSetStats,
                            MagicIcon.STATS,
                            "[DevMode] Save statistics",
                            "Creates CardStatistics.txt to view current card completion."
                    )
            );
        }
    }

    private void doShowScriptScreen() {
        contentPanel.showCardScriptScreen();
    }

    private void doSelectRandomCard() {
        contentPanel.selectRandomCard();
    }

    private void doCreateSetStats() {
        try {
            MagicSetDefinitions.createSetStatistics();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void doSwitchLayout() {
        ExplorerScreenLayout.setNextLayout();
        contentPanel.refreshLayout();
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

    private void doSaveMissingCardsFile() {
        try {
            saveMissingCardsList();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean isScreenReadyToClose(final Object nextScreen) {
        if (super.isScreenReadyToClose(nextScreen)) {
            MagicSetDefinitions.clearLoadedSets();
            MagicLogs.clearLoadedLogs();
            return true;
        }
        return false;
    }

    public void refreshTotals(int total, int playable, int missing) {
        headerPanel.refreshTotals(total, playable, missing);
    }
}
