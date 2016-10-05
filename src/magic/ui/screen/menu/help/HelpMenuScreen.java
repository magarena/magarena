package magic.ui.screen.menu.help;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.ui.ScreenController;
import magic.ui.URLUtils;
import magic.translate.UiString;
import magic.ui.screen.AbstractScreen;
import magic.ui.screen.widget.MenuPanel;
import magic.ui.widget.KeysStripPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class HelpMenuScreen extends AbstractScreen {

    // translatable strings
    private static final String _S1 = "Help Menu";
    private static final String _S2 = "ReadMe";
    private static final String _S3 = "Online help";
    private static final String _S4 = "Keywords glossary";
    private static final String _S5 = "About Magarena";
    private static final String _S6 = "Close menu";

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

        final MenuPanel menu = new MenuPanel(UiString.get(_S1));

        menu.addMenuItem(UiString.get(_S2), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.showReadMeScreen();
            }
        });
        menu.addMenuItem(UiString.get(_S3), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                URLUtils.openURL(URLUtils.URL_USERGUIDE);
            }
        });
        menu.addMenuItem(UiString.get(_S4), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.showKeywordsScreen();
            }
        });
        menu.addMenuItem(UiString.get(_S5), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.showAboutScreen();
            }
        });
        menu.addBlankItem();
        menu.addMenuItem(UiString.get(_S6), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.closeActiveScreen(false);
            }
        });

        return menu;

    }

}
