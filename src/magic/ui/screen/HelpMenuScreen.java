package magic.ui.screen;

import magic.data.URLUtils;
import magic.ui.dialog.AboutDialog;
import magic.ui.screen.widget.MenuPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class HelpMenuScreen extends AbstractScreen {
   

    public HelpMenuScreen() {
        setContent(getScreenContent());
    }

    private JPanel getScreenContent() {

        final JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new MigLayout("insets 0, gap 0, center, center"));

        final MenuPanel menu = new MenuPanel("Help Menu");

        menu.addMenuItem("ReadMe", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                getFrame().showReadMeScreen();
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
                getFrame().showKeywordsScreen();
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
                getFrame().closeActiveScreen(false);
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
