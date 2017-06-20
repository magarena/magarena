package magic.ui.screen.card.explorer;

import magic.data.GeneralConfig;
import magic.data.MagicIcon;
import magic.data.MagicSetDefinitions;
import magic.translate.MText;
import magic.ui.MagicLogs;
import magic.ui.WikiPage;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.screen.MScreen;
import magic.ui.screen.widget.PlainMenuButton;
import magic.ui.widget.cards.table.CardsTableStyle;

@SuppressWarnings("serial")
public class ExplorerScreen extends HeaderFooterScreen {

    // translatable strings
    private static final String _S1 = "Card Explorer";
    private static final String _S3 = "View Script";
    private static final String _S4 = "View the script and groovy files for the selected card.<br>(or double-click row)";
    private static final String _S5 = "Lucky Dip";
    private static final String _S6 = "Selects a random card from the list of cards displayed.";

    private ExplorerContentPanel contentPanel;
    private ExplorerHeaderPanel headerPanel;

    public ExplorerScreen() {
        super(MText.get(_S1));
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
        setHeaderOptions(new OptionsPanel(this));
        setMainContent(contentPanel);
        setFooterButtons();
        setWikiPage(WikiPage.CARDS_EXPLORER);
    }

    private void setFooterButtons() {
        addToFooter(PlainMenuButton.build(this::doShowScriptScreen,
                        MagicIcon.EDIT, MText.get(_S3), MText.get(_S4)
                ),
                PlainMenuButton.build(this::doSelectRandomCard,
                        MagicIcon.RANDOM, MText.get(_S5), MText.get(_S6)
                )
        );
    }

    private void doShowScriptScreen() {
        contentPanel.showCardScriptScreen();
    }

    private void doSelectRandomCard() {
        contentPanel.selectRandomCard();
    }

    public void doSwitchLayout() {
        ExplorerScreenLayout.setNextLayout();
        contentPanel.refreshLayout();
    }

    private void doSaveSettings() {
        ExplorerScreenLayout.save();
        CardsTableStyle.save();
        GeneralConfig.getInstance().save();
    }

    @Override
    public boolean isScreenReadyToClose(MScreen nextScreen) {
        if (super.isScreenReadyToClose(nextScreen)) {
            MagicSetDefinitions.clearLoadedSets();
            MagicLogs.clearLoadedLogs();
            doSaveSettings();
            return true;
        }
        return false;
    }

    public void refreshTotals(int total, int playable, int missing) {
        headerPanel.refreshTotals(total, playable, missing);
    }

    void setCardsTableStyle(int dialPosition) {
        CardsTableStyle.setStyle(dialPosition);
        contentPanel.setCardsTableStyle();
    }
}
