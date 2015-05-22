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
import magic.utility.MagicSystem;
import magic.data.CardDefinitions;
import magic.data.MagicIcon;
import magic.ui.IconImages;
import magic.data.MagicSetDefinitions;
import magic.ui.explorer.ExplorerPanel;
import magic.ui.MagicFrame;
import magic.ui.ScreenOptionsOverlay;
import magic.ui.dialog.DownloadImagesDialog;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IOptionsMenu;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.interfaces.IWikiPage;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.MenuButton;
import magic.ui.screen.widget.MenuPanel;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;

@SuppressWarnings("serial")
public class CardExplorerScreen
    extends AbstractScreen
    implements IStatusBar, IActionBar, IOptionsMenu, IWikiPage {

    private final ExplorerPanel content;

    public CardExplorerScreen() {
        content = new ExplorerPanel();
        setContent(content);
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagStatusBar#getScreenCaption()
     */
    @Override
    public String getScreenCaption() {
        return "Card Explorer";
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getLeftAction()
     */
    @Override
    public MenuButton getLeftAction() {
        return MenuButton.getCloseScreenButton("Close");
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getRightAction()
     */
    @Override
    public MenuButton getRightAction() {
        return null;
    }

    @Override
    public List<MenuButton> getMiddleActions() {
        final List<MenuButton> buttons = new ArrayList<>();
        buttons.add(
                new ActionBarButton(
                        IconImages.getIcon(MagicIcon.EDIT_ICON),
                        "View Script", "View the script and groovy files for the selected card.<br>(or double-click row)",
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                content.showCardScriptScreen();
                            }
                        })
        );
        if (MagicSystem.isDevMode() || MagicSystem.isDebugMode()) {
            buttons.add(
                    new ActionBarButton(
                            IconImages.getIcon(MagicIcon.SAVE_ICON),
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
                            })
            );
        }
        return buttons;
    }

    private void saveMissingCardsList() throws IOException {
        final List<String> missingCards = CardDefinitions.getMissingCardNames();
        Collections.sort(missingCards);
        final Path savePath = MagicFileSystem.getDataPath(DataPath.LOGS).resolve("CardsMissingInMagarena.txt");
        try (final PrintWriter writer = new PrintWriter(savePath.toFile())) {
            for (final String cardName : missingCards) {
                writer.println(cardName);
            }
        }
        Desktop.getDesktop().open(MagicFileSystem.getDataPath(DataPath.LOGS).toFile());
    }

    /* (non-Javadoc)
     * @see magic.ui.MagScreen#canScreenClose()
     */
    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        MagicSetDefinitions.clearLoadedSets();
        DownloadImagesDialog.clearLoadedLogs();
        return true;
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagScreenOptionsMenu#showOptionsMenuOverlay()
     */
    @Override
    public void showOptionsMenuOverlay() {
        new ScreenOptions(getFrame());
    }

    @Override
    public String getWikiPageName() {
        return "UICardExplorer";
    }

    private class ScreenOptions extends ScreenOptionsOverlay {

        public ScreenOptions(final MagicFrame frame) {
            super(frame);
        }

        /* (non-Javadoc)
         * @see magic.ui.ScreenOptionsOverlay#getScreenMenu()
         */
        @Override
        protected MenuPanel getScreenMenu() {
            return null;
        }

    }

    /* (non-Javadoc)
     * @see magic.ui.screen.interfaces.IStatusBar#getStatusPanel()
     */
    @Override
    public JPanel getStatusPanel() {
        return null;
    }

}
