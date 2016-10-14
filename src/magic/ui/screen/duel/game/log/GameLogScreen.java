package magic.ui.screen.duel.game.log;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import magic.data.MagicIcon;
import magic.translate.UiString;
import magic.ui.ScreenController;
import magic.ui.helpers.KeyEventAction;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.screen.widget.MenuButton;
import magic.ui.widget.M.MTextFileViewer;
import magic.utility.MagicFileSystem;

@SuppressWarnings("serial")
public class GameLogScreen extends HeaderFooterScreen {

    // translatable strings
    private static final String _S1 = "Basic";
    private static final String _S2 = "Complete";
    private static final String _S4 = "Basic log";
    private static final String _S5 = "Displays an abbreviated log, omitting the AI's thoughts.";
    private static final String _S6 = "Complete log";
    private static final String _S7 = "Displays the complete log file with insight into how the AI thinks.";
    private static final String _S8 = "Game log";

    private static final Path LOGFILE = MagicFileSystem.getGameLogPath();

    // static so it restores the last view when the screen is next opened.
    private static boolean isBasicLog = true;

    private final MTextFileViewer mainView = new MTextFileViewer();
    private final JLabel modeLabel = new JLabel(UiString.get(_S1));

    public GameLogScreen() {
        super(UiString.get(_S8));
        setDefaultProperties();
        setHeaderContent(modeLabel);
        setMainContent(mainView.component());
        reloadTextFile();
    }

    private void setDefaultProperties() {

        modeLabel.setForeground(Color.WHITE);
        modeLabel.setFont(modeLabel.getFont().deriveFont(16f));
        modeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        KeyEventAction.doAction(this, ()->ScreenController.closeActiveScreen())
            .on(0, KeyEvent.VK_L);
    }

    private void setFooterButtons() {
        clearFooterButtons();
        if (isBasicLog) {
            addToFooter(MenuButton.build(this::showDetailedLog, 
                    MagicIcon.SWAP, UiString.get(_S6), UiString.get(_S7))
            );
            modeLabel.setText(UiString.get(_S1));
        } else {
            addToFooter(MenuButton.build(this::showBasicLog, 
                    MagicIcon.SWAP, UiString.get(_S4), UiString.get(_S5))
            );
            modeLabel.setText(UiString.get(_S2));
        }
    }

    private void reloadTextFile() {
        mainView.setTextFile(LOGFILE, this::logFilter);
        setFooterButtons();
    }

    /**
     * If running in basic mode strips out any AI diagnostics.
     */
    private String logFilter(String text) {
        return isBasicLog
                ? Stream.of(text.split("\n"))
                        .filter(line -> line.startsWith("LOG"))
                        .map(line -> line.substring(3).trim())
                        .collect(Collectors.joining("\n"))
                : text;
    }

    private void showBasicLog() {
        isBasicLog = true;
        reloadTextFile();
    }

    private void showDetailedLog() {
        isBasicLog = false;
        reloadTextFile();
    }
}
