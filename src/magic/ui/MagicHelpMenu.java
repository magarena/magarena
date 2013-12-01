package magic.ui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import magic.data.URLUtils;
import magic.ui.widget.MenuPanel;

@SuppressWarnings("serial")
public class MagicHelpMenu extends MenuPanel {

    private static final String DOCUMENTATION_URL = "http://code.google.com/p/magarena/wiki/AboutMagarena?tm=6";

    public MagicHelpMenu(final MagicFrame frame0, final IMagicHelpOverlay overlay, final boolean addCloseMenuItem) {

        super("Help Menu");

        addMenuItem("ReadMe", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame0.showReadMeScreen();;
                if (overlay != null) {
                    overlay.actionPerformed();
                }
            }
        });
        addMenuItem("Online help", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                URLUtils.openURL(DOCUMENTATION_URL);
            }
        });
        addMenuItem("Keywords glossary", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame0.showKeywordsScreen();
                if (overlay != null) {
                    overlay.actionPerformed();
                }
            }
        });
        addMenuItem("About Magarena", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AboutDialog(frame0);
            }
        });
        if (addCloseMenuItem) {
            addBlankItem();
            addMenuItem("Close menu", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (overlay == null) {
                        frame0.closeActiveScreen(false);
                    } else {
                        overlay.actionPerformed();
                    }
                }
            });
        }

        refreshLayout();

    }

    public MagicHelpMenu(final MagicFrame frame0) {
        this(frame0, null, true);
    }
}
