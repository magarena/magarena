package magic.ui.screen;

import magic.MagicMain;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.MenuButton;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class GameLogScreen extends TextFileReaderScreen implements IStatusBar {

    private static final Path LOGS_DIRECTORY = Paths.get(MagicMain.getLogsPath());
    private static final Path TEXT_FILE = LOGS_DIRECTORY.resolve("game.log");
    private boolean isBasicView = true;

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
        final List<MenuButton> buttons = new ArrayList<MenuButton>();
        if (!isBasicView) {
            buttons.add(
                new ActionBarButton(
                        "Basic View", "Filters log file to remove AI diagnostics.",
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
                        "Detailed View", "Full log file, including AI diagnostics.",
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                isBasicView = false;
                                reloadTextFile();
                            }
                        }));
        };
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
