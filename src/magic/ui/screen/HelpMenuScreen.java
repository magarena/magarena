package magic.ui.screen;

import magic.data.URLUtils;
import magic.ui.dialog.AboutDialog;
import magic.ui.screen.widget.MenuPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import magic.ui.ScreenController;
import magic.ui.widget.KeysStripPanel;

@SuppressWarnings("serial")
public class HelpMenuScreen extends AbstractScreen {
   

    public HelpMenuScreen() {
        setContent(getScreenContent());
    }

    private JPanel getScreenContent() {

        final MenuPanel menuPanel = getMenuPanel();
        menuPanel.refreshLayout();

        final JPanel content = new JPanel();
        content.setOpaque(false);

        final MigLayout layout = new MigLayout();
        layout.setLayoutConstraints("insets 0, gap 0, flowy");
        layout.setRowConstraints("[30!][100%, center][30!, bottom]");
        content.setLayout(layout);
        content.add(new JLabel(), "w 100%, h 100%");
        content.add(menuPanel, "w 100%, alignx center");
        content.add(new KeysStripPanel(), "w 100%");

        return content;
    }

    private MenuPanel getMenuPanel() {

        final MenuPanel menu = new MenuPanel("Help Menu");

        menu.addMenuItem("ReadMe", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.showReadMeScreen();
            }
        });
        menu.addMenuItem("Online help", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                URLUtils.openURL(URLUtils.URL_USERGUIDE);
            }
        });
        menu.addMenuItem("Keywords glossary", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.showKeywordsScreen();
            }
        });
        menu.addMenuItem("About Magarena", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                new AboutDialog(getFrame());
            }
        });
        menu.addBlankItem();
        menu.addMenuItem("Close menu", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.closeActiveScreen(false);
            }
        });

        return menu;

    }

    /* (non-Javadoc)
     * @see magic.ui.MagScreen#isScreenReadyToClose(magic.ui.MagScreen)
     */
    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        return true;
    }

}
