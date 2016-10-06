package magic.ui.screen.duel.game.log;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import magic.data.MagicIcon;
import magic.model.MagicGameLog;
import magic.ui.utility.DesktopUtils;
import magic.ui.MagicImages;
import magic.ui.ScreenController;
import magic.translate.UiString;
import magic.ui.screen.TextFileReaderScreen;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.MenuButton;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;

@SuppressWarnings("serial")
public class GameLogScreen extends TextFileReaderScreen implements IStatusBar {

    private static boolean isBasicView = true;

    // translatable strings
    private static final String _S1 = "Logs directory";
    private static final String _S2 = "Opens the logs directory containing '%s' in the default file explorer.";
    private static final String _S3 = "Could not open 'logs' directory : %s";
    private static final String _S4 = "Basic View";
    private static final String _S5 = "Filters log file to remove AI diagnostics.";
    private static final String _S6 = "Detailed View";
    private static final String _S7 = "Full log file, including AI diagnostics.";

    private static final Path LOGS_DIRECTORY = MagicFileSystem.getDataPath(DataPath.LOGS);
    private static final Path TEXT_FILE = LOGS_DIRECTORY.resolve(MagicGameLog.LOG_FILE);

    public GameLogScreen() {
        reloadTextFile();
    }

    private void reloadTextFile() {
        setTextFile(TEXT_FILE);
        refreshActionBar();
    }

    @Override
    protected String reprocessFileContents(String fileContent) {
        if (isBasicView) {
            final String[] text = fileContent.split("\n");
            final StringBuffer sb = new StringBuffer();
            for (String line : text) {
                if (line.startsWith("LOG")) {
                    sb.append(line.substring(3).trim()).append("\n");
                }
            }
            return sb.toString();
        } else {
            return fileContent;
        }
    }

    @Override
    public List<MenuButton> getMiddleActions() {
        final List<MenuButton> buttons = new ArrayList<>();
        if (!isBasicView) {
            buttons.add(new ActionBarButton(
                        MagicImages.getIcon(MagicIcon.OPEN),
                        UiString.get(_S1), UiString.get(_S2, MagicGameLog.LOG_FILE),
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    DesktopUtils.openMagicDirectory(DataPath.LOGS);
                                } catch (IOException ex) {
                                    ScreenController.showWarningMessage(UiString.get(_S3, ex.getMessage()));
                                }
                            }
                        }));
            buttons.add(
                new ActionBarButton(
                        UiString.get(_S4), UiString.get(_S5),
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                isBasicView = true;
                                reloadTextFile();
                            }
                        }));
        } else {
            buttons.add(
                new ActionBarButton(
                        UiString.get(_S6), UiString.get(_S7),
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                isBasicView = false;
                                reloadTextFile();
                            }
                        }));
        }
        return buttons;
    }

    @Override
    public String getScreenCaption() {
        return TEXT_FILE.getFileName().toString();
    }

    @Override
    public JPanel getStatusPanel() {
        return null;
    }

}
