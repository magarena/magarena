package magic.ui.screen.stats;

import magic.data.stats.MagicStats;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.screen.MScreen;
import magic.ui.screen.widget.PlainMenuButton;

@SuppressWarnings("serial")
public class StatsScreen extends HeaderFooterScreen {

    private final StatsContentPanel contentPanel;
    private final StatsHeaderPanel headerPanel;

    public StatsScreen() {
        super("Stats");

        headerPanel = new StatsHeaderPanel(MagicStats.getSchemaVersion());
        setHeaderContent(headerPanel);

        contentPanel = new StatsContentPanel(this);
        setMainContent(contentPanel);

        setLeftFooter(PlainMenuButton.getCloseScreenButton());

        // Optional or one button allowed.
        //setRightFooter(MenuButton.getTestButton());

        // adds a variable number of MenuButtons to central footer.
//        addToFooter(
//                MenuButton.getTestButton(),
//                MenuButton.build(this::showTestMessage,
//                        MagicIcon.STATS,
//                        "Testing", "Click to test...")
//            );
    }

    @Override
    public boolean isScreenReadyToClose(MScreen aScreen) {
        return super.isScreenReadyToClose(aScreen)
                && contentPanel.isReadyToClose();
    }

    void setTotalGames(int totalGames) {
        headerPanel.refreshTotals(totalGames);
    }

}
