package magic.ui.screen;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

import magic.data.URLUtils;
import magic.ui.DownloadImagesDialog;
import magic.ui.MagicFrame;
import magic.ui.widget.MenuPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class SettingsMenuScreen extends AbstractScreen {

    private static DownloadImagesDialog downloadDialog;

    public SettingsMenuScreen(final MagicFrame frame0) {
        super(getScreenContent(frame0), frame0);
    }

    private static JPanel getScreenContent(final MagicFrame frame0) {

        final JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new MigLayout("insets 0, gap 0, center, center"));

        final MenuPanel menu = new MenuPanel("Settings Menu");

        menu.addMenuItem("Preferences", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                frame0.openPreferencesDialog();
            }
        });
        menu.addMenuItem("Download images", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (downloadDialog == null || !downloadDialog.isDisplayable()) {
                    downloadDialog = new DownloadImagesDialog(frame0);
                }
                downloadDialog.setState(Frame.NORMAL);
                downloadDialog.setVisible(true);
            }
        });
        menu.addMenuItem("More themes online...", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                URLUtils.openURL("http://www.slightlymagic.net/forum/viewforum.php?f=89");
            }
        });
        menu.addMenuItem("Toggle full-screen", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                frame0.toggleFullScreenMode();
            }
        }, "Shortcut key: F11");
        menu.addBlankItem();
        menu.addMenuItem("Close menu", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                frame0.closeActiveScreen(false);
            }
        });

        menu.refreshLayout();
        content.add(menu);
        return content;

    }

    /* (non-Javadoc)
     * @see magic.ui.MagScreen#isScreenReadyToClose(magic.ui.MagScreen)
     */
    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        return true;
    }

}
