package magic.ui.screen;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import magic.ui.ScreenController;
import magic.ui.UiString;
import magic.ui.screen.widget.MenuPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class StartScreen extends AbstractScreen {

    public StartScreen() {

        if (isPostInstallStartup() == false) {
            showMainMenuScreen();
        }

        setContent(new ScreenContent());

    }

    private boolean isPostInstallStartup() {
        return true;
    }

    private class ScreenContent extends JPanel {

        private final MigLayout migLayout = new MigLayout();

        public ScreenContent() {

            setOpaque(false);
            setLayout(migLayout);

            showLanguageMenu();

        }

        private void showLanguageMenu() {

            final MenuPanel menuPanel = new MenuPanel("Language?");

            menuPanel.addMenuItem(UiString.get("English"), new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    showMainMenuScreen();
                }
            });

            menuPanel.addMenuItem(UiString.get("Português"), new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    showMainMenuScreen();
                }
            });

            menuPanel.refreshLayout();

//            final JPanel content = new JPanel();
//            content.setOpaque(false);

              migLayout.setLayoutConstraints("alignx center, aligny center");
//            migLayout.setLayoutConstraints("insets 0, gap 0, flowy");
//            migLayout.setRowConstraints("[30!][100%, center][30!]");
//            add(new JLabel(), "w 100%, h 100%");
            add(menuPanel); //, "w 100%, h 400!");

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
