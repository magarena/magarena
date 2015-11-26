package magic.ui.duel.textmode;

import magic.ui.duel.SwingGameController;
import magic.ui.theme.Theme;

class ExileViewer extends CardListViewer {
    private static final long serialVersionUID = 1L;

    ExileViewer(final SwingGameController controller, final boolean opponent) {
        super(
            controller,
            controller.getViewerInfo().getPlayerInfo(opponent).exile,
            "Exile : " + controller.getViewerInfo().getPlayerInfo(opponent).name,
            Theme.ICON_SMALL_EXILE
        );

        update();
    }
}
