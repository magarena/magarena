package magic.ui.screen.duel.game;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import magic.translate.MText;
import magic.ui.ScreenController;
import magic.ui.ScreenOptionsOverlay;
import magic.ui.screen.widget.MenuPanel;

@SuppressWarnings("serial")
class GameOptionsOverlay extends ScreenOptionsOverlay {

    // translatable strings
    private static final String _S1 = "Game Options";
    private static final String _S2 = "Concede game";
    private static final String _S3 = "Restart game";
    private static final String _S7 = "Sidebar Layout";
    private static final String _S8 = "Resume game";
    private static final String _S9 = "Gameplay Report";

    private final SwingGameController controller;

    GameOptionsOverlay(SwingGameController controller) {
        this.controller = controller;
        controller.setGamePaused(true);
    }

    @Override
    protected MenuPanel getScreenMenu() {

        final MenuPanel menu = new MenuPanel(MText.get(_S1));

        menu.addMenuItem(MText.get(_S2), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                controller.concede();
                hideOverlay();
            }
        });
        menu.addMenuItem(MText.get(_S3), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                controller.resetGame();
                hideOverlay();
            }
        });
        menu.addMenuItem(MText.get(_S7), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                hideOverlay();
                ScreenController.showDuelSidebarDialog(controller);
            }
        });
        menu.addBlankItem();
        menu.addMenuItem(MText.get(_S9), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                controller.createGameplayReport();
                hideOverlay();
            }
        });
        menu.addBlankItem();
        menu.addMenuItem(MText.get(_S8), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                hideOverlay();
            }
        });

        menu.refreshLayout();
        return menu;

    }

    @Override
    protected boolean showPreferencesOption() {
        return true;
    }

    @Override
    public void hideOverlay() {
        setVisible(false);
        controller.setGamePaused(false);
    }
}
