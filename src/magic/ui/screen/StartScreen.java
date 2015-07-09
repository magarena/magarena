package magic.ui.screen;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import magic.data.GeneralConfig;
import magic.ui.ScreenController;
import magic.ui.UiString;
import magic.ui.screen.widget.MenuPanel;
import magic.utility.MagicSystem;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class StartScreen extends AbstractScreen {

    public StartScreen() {
        if (MagicSystem.isNewInstall() == false) {
            showMainMenuScreen();
        } else {
            setContent(new ScreenContent());
        }
    }

    private class ScreenContent extends JPanel {

        private final MigLayout migLayout = new MigLayout();

        public ScreenContent() {

            setOpaque(false);
            setLayout(migLayout);

            showLanguageMenu();

        }

        private void setLanguage(String aLanguage) throws FileNotFoundException {
            GeneralConfig.getInstance().setTranslation(aLanguage);
            GeneralConfig.getInstance().save();
            UiString.loadTranslationFile();
        }

        private void showLanguageMenu() {

            final MenuPanel menuPanel = new MenuPanel("Language");

            menuPanel.addMenuItem(UiString.get("English"), new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent ev) {
                    try {
                        setLanguage(GeneralConfig.DEFAULT_TRANSLATION);
                        showMainMenuScreen();                        
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            menuPanel.addMenuItem(UiString.get("Português"), new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    try {
                        setLanguage("Português");
                        showMainMenuScreen();
                    } catch (FileNotFoundException ex) {
                        ScreenController.showWarningMessage("There is a problem with the translation file : " + ex);
                    }
                }
            });

            menuPanel.refreshLayout();

            migLayout.setLayoutConstraints("alignx center, aligny center");
            add(menuPanel);

        }

    }

    private void showMainMenuScreen() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ScreenController.showMainMenuScreen();
            }
        });
    }

    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        return true;
    }

}
