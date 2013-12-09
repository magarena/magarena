package magic.ui.screen;

import java.awt.event.ActionEvent;

import magic.ui.MagicFrame;
import magic.ui.widget.MenuPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainMenuScreen extends AbstractScreen {

    private static MenuPanel menuPanel;

    public MainMenuScreen(final MagicFrame frame) {
        super(getScreenContent(frame), frame);
    }

    private static JPanel getScreenContent(final MagicFrame frame) {

        final JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new MigLayout("insets 0, gap 0, center, center"));

        menuPanel = new MenuPanel("Main Menu");
        content.add(menuPanel);

        menuPanel.addMenuItem("New duel", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                frame.showNewDuelDialog();
            }
        });
        menuPanel.addMenuItem("Load duel", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                frame.loadDuel();
            }
        });
        menuPanel.addMenuItem("Card explorer", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                frame.showCardExplorerScreen();
            }
        });
        menuPanel.addMenuItem("Deck editor", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                frame.showDeckEditor();
            }
        });
        menuPanel.addMenuItem("Settings", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                frame.showSettingsMenuScreen();
            }
        });
        menuPanel.addMenuItem("Help", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                frame.showHelpMenuScreen();
            }
        });
        menuPanel.addMenuItem("Quit to desktop", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                frame.closeActiveScreen(false);
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
