package magic.ui.screen.menu.language;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import magic.data.GeneralConfig;
import magic.ui.ScreenController;
import magic.translate.UiString;
import magic.ui.screen.AbstractScreen;
import magic.ui.screen.widget.MenuPanel;
import magic.utility.MagicFileSystem;
import magic.utility.MagicSystem;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class StartScreen extends AbstractScreen {

    // translatable strings
    private static final String _S1 = "Language";
    private static final String _S2 = "Invalid translation file.";

    private List<String> translations;

    public StartScreen() {
        // use of runnable fixes #731 (https://github.com/magarena/magarena/issues/731)
        SwingUtilities.invokeLater(() -> {
            if (MagicSystem.isNewInstall() == false) {
                showMainMenuScreen();
            } else {
                setContent(new ScreenContent());
            }
        });
    }

    private class ScreenContent extends JPanel {

        private final MigLayout migLayout = new MigLayout();

        public ScreenContent() {

            setOpaque(false);
            setLayout(migLayout);

            translations = MagicFileSystem.getTranslationFilenames();
            if (translations.isEmpty()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        ScreenController.showImportScreen();
                    }
                });
            } else {
                showLanguageMenu();
            }

        }

        private void setLanguage(String aLanguage) throws FileNotFoundException {
            GeneralConfig.getInstance().setTranslation(aLanguage);
            UiString.loadTranslationFile();
            GeneralConfig.getInstance().save();
        }

        private void showLanguageMenu() {

            final MenuPanel menuPanel = new MenuPanel(UiString.get(_S1));

            menuPanel.addMenuItem(UiString.get("English"), new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent ev) {
                    try {
                        setLanguage(GeneralConfig.DEFAULT_TRANSLATION);
                        ScreenController.showImportScreen();
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            for (final String translation : translations) {
                menuPanel.addMenuItem(translation, new AbstractAction() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        try {
                            setLanguage(translation);
                            ScreenController.showImportScreen();
                        } catch (FileNotFoundException | NumberFormatException ex) {
                            System.err.println(ex);
                            ScreenController.showWarningMessage(
                                    String.format("%s\n\n%s", UiString.get(_S2), ex)
                            );
                        }
                    }
                });
            }

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

}
