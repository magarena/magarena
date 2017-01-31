package magic.ui.screen.stats;

import java.sql.SQLException;
import magic.data.stats.MagicStats;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.screen.widget.MenuButton;

@SuppressWarnings("serial")
public class StatsScreen extends HeaderFooterScreen {

    private final StatsContentPanel contentPanel;
    private final StatsHeaderPanel headerPanel;

    public StatsScreen() {
        super("Stats");

        headerPanel = new StatsHeaderPanel(MagicStats.getSchemaVersion());
        setHeaderContent(headerPanel);

        try {
            contentPanel = new StatsContentPanel(this);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        setMainContent(contentPanel);

        setLeftFooter(MenuButton.getCloseScreenButton());

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
    public boolean isScreenReadyToClose(Object aScreen) {
        return super.isScreenReadyToClose(aScreen)
                && contentPanel.isReadyToClose();
    }

    void setTotalGames(int totalGames) {
        headerPanel.refreshTotals(totalGames);
    }

}
