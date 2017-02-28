package magic.ui.screen.player;

import magic.data.MagicIcon;
import magic.model.player.PlayerProfiles;
import magic.ui.ScreenController;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.screen.card.explorer.ExplorerHeaderPanel;
import magic.ui.screen.widget.MenuButton;

@SuppressWarnings("serial")
public class PlayerScreen extends HeaderFooterScreen {

    public PlayerScreen() {

        // mandatory screen title, everything else is optional.
        super("Player");

        // main JPanel where everything happens.
        setMainContent(new ScreenPanel());

        // display a JPanel of stuff in central header.
        setHeaderContent(new ExplorerHeaderPanel());

        // adds a default "Close" button if not specified.
        setLeftFooter(MenuButton.getTestButton());

        // Optional or one button allowed.
        setRightFooter(MenuButton.getTestButton());

        // adds a variable number of MenuButtons to central footer.
        addToFooter(
                MenuButton.getTestButton(),
                MenuButton.build(this::showTestMessage,
                        MagicIcon.STATS,
                        "Testing", "Click to test...")
            );
    }

    public PlayerScreen(String guid) {
        super(PlayerProfiles.getPlayerProfiles().get(guid).getPlayerName());
    }

    private void showTestMessage() {
        ScreenController.showInfoMessage("Testing...");
    }
}
