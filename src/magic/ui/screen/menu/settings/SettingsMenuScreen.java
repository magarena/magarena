package magic.ui.screen.menu.settings;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import magic.data.GeneralConfig;
import magic.translate.UiString;
import magic.ui.ScreenController;
import magic.ui.dialog.FiremindWorkerDialog;
import magic.ui.screen.AbstractScreen;
import magic.ui.screen.widget.MenuPanel;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.KeysStripPanel;
import magic.utility.MagicFileSystem;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class SettingsMenuScreen extends AbstractScreen {

    // translatable strings.
    private static final String _S1 = "Settings Menu";
    private static final String _S2 = "Preferences";
    private static final String _S5 = "Download card images";
    private static final String _S6 = "Run Firemind Worker";
    private static final String _S7 = "Toggle full-screen";
    private static final String _S9 = "Close menu";
    private static final String _S10 = "Reset & Restart";
    private static final String _S11 = "Reset settings to default and run startup wizard?";
    private static final String _S12 = "This action cannot be undone!";
    private static final String _S13 = "Reset & Restart";
    private static final String _S14 = "Yes";
    private static final String _S15 = "No";
    private static final String _S16 = "Please switch off the 'Download images on demand' setting in preferences first.";

    private static FiremindWorkerDialog firemindWorkerDialog;

    public SettingsMenuScreen() {
        setContent(getScreenContent());
    }

    private JPanel getScreenContent() {

        final MenuPanel menuPanel = new MenuPanel(UiString.get(_S1));

        menuPanel.addMenuItem(UiString.get(_S2), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.showPreferencesDialog();
            }
        });

        menuPanel.addMenuItem(UiString.get(_S5), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (GeneralConfig.getInstance().getImagesOnDemand()) {
                    ScreenController.showInfoMessage(UiString.get(_S16));
                } else {
                    ScreenController.showDownloadImagesScreen();
                }
            }
        });

        menuPanel.addMenuItem(UiString.get(_S6), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (firemindWorkerDialog == null || !firemindWorkerDialog.isDisplayable()) {
                    firemindWorkerDialog = new FiremindWorkerDialog(getFrame());
                } else {
                    firemindWorkerDialog.setVisible(true);
                }
            }
        });

        menuPanel.addMenuItem(UiString.get(_S7), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                getFrame().toggleFullScreenMode();
            }
        });

        menuPanel.addMenuItem(UiString.get(_S10), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                doReset();
            }
        });

        menuPanel.addBlankItem();
        menuPanel.addMenuItem(UiString.get(_S9), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.closeActiveScreen(false);
            }
        });

        menuPanel.refreshLayout();

        final MigLayout layout = new MigLayout();
        layout.setLayoutConstraints("insets 0, gap 0, flowy");
        layout.setRowConstraints("[30!][100%, center][30!, bottom]");
        layout.setColumnConstraints("[center]");

        final JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(layout);
        content.add(menuPanel, "cell 0 1");
        content.add(new KeysStripPanel(), "w 100%, cell 0 2");

        return content;

    }

    private void doReset() {
        final int response = JOptionPane.showOptionDialog(
                ScreenController.getMainFrame(),
                String.format("<html>%s<br><br><b>%s</b></html>", UiString.get(_S11), UiString.get(_S12)),
                UiString.get(_S13),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{UiString.get(_S14), UiString.get(_S15)}, UiString.get(_S15));
        if (response == JOptionPane.YES_OPTION) {
            setVisible(false);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    MagicFileSystem.deleteGeneralConfigFile();
                    GeneralConfig.getInstance().load();
                    ThemeFactory.getInstance().setCurrentTheme(GeneralConfig.getInstance().getTheme());
                    ScreenController.showStartScreen();
                }
            });

        }
    }

    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        return true;
    }

}
