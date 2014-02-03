package magic.ui.screen;

import java.awt.event.ActionEvent;

import magic.ui.MagicFrame;
import magic.ui.screen.widget.MenuPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainMenuScreen extends AbstractScreen {

    public MainMenuScreen(final MagicFrame frame) {
        setContent(getScreenContent());
    }

    private JPanel getScreenContent() {

        final JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new MigLayout("insets 0, gap 0, center, center"));

        final MenuPanel menuPanel = new MenuPanel("Main Menu");
        content.add(menuPanel);

        menuPanel.addMenuItem("New duel", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                getFrame().showNewDuelDialog();
            }
        });
        menuPanel.addMenuItem("Load duel", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                getFrame().loadDuel();
            }
        });
        menuPanel.addMenuItem("Card explorer", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                System.out.println(getFrame());
                getFrame().showCardExplorerScreen();
            }
        });
        menuPanel.addMenuItem("Deck editor", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                getFrame().showDeckEditor();
            }
        });
        menuPanel.addMenuItem("Settings", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                getFrame().showSettingsMenuScreen();
            }
        });
        menuPanel.addMenuItem("Help", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                getFrame().showHelpMenuScreen();
            }
        });
        menuPanel.addMenuItem("Quit to desktop", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                getFrame().closeActiveScreen(false);
            }
        });

        menuPanel.refreshLayout();
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
