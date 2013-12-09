package magic.ui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import magic.data.URLUtils;
import magic.ui.widget.MenuPanel;

@SuppressWarnings("serial")
public class GeneralMenuPanel extends MenuPanel {

    private static final String DOCUMENTATION_URL = "http://code.google.com/p/magarena/wiki/AboutMagarena?tm=6";

    public GeneralMenuPanel(final MagicFrame frame, final IMenuOverlay overlay) {

        super("General Options");

        // Help stuff.
        addMenuItem("ReadMe", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                frame.showReadMeScreen();
                overlay.hideOverlay();
            }
        });
        addMenuItem("Online help", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                URLUtils.openURL(DOCUMENTATION_URL);
                overlay.hideOverlay();
            }
        });
        addMenuItem("Keywords glossary", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                frame.showKeywordsScreen();
                overlay.hideOverlay();
            }
        });
        addBlankItem();

        // System stuff.
        addMenuItem("Preferences", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                overlay.hideAllMenuPanels();
                frame.openPreferencesDialog();
                overlay.hideOverlay();
            }
        });
        addBlankItem();

        addMenuItem("Quit to main menu", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                frame.showMainMenuScreen();
                overlay.hideOverlay();
            }
        });
        addMenuItem("Quit to desktop", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                frame.quitToDesktop(false);
            }
        });

        refreshLayout();
    }

}
