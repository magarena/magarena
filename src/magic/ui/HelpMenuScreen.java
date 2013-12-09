package magic.ui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

import magic.data.URLUtils;
import magic.ui.widget.MenuPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class HelpMenuScreen extends MagScreen {

    private static final String DOCUMENTATION_URL = "http://code.google.com/p/magarena/wiki/AboutMagarena?tm=6";

    public HelpMenuScreen(final MagicFrame frame0) {
        super(getScreenContent(frame0), frame0);
    }

    private static JPanel getScreenContent(final MagicFrame frame0) {

        final JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new MigLayout("insets 0, gap 0, center, center"));

        final MenuPanel menu = new MenuPanel("Settings Menu");

        menu.addMenuItem("ReadMe", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                frame0.showReadMeScreen();
            }
        });
        menu.addMenuItem("Online help", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                URLUtils.openURL(DOCUMENTATION_URL);
            }
        });
        menu.addMenuItem("Keywords glossary", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                frame0.showKeywordsScreen();
            }
        });
        menu.addMenuItem("About Magarena", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                new AboutDialog(frame0);
            }
        });
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
    public boolean isScreenReadyToClose(final MagScreen nextScreen) {
        return true;
    }

}
