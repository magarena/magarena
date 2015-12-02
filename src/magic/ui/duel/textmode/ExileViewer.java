package magic.ui.duel.textmode;

import magic.ui.duel.SwingGameController;
import magic.ui.theme.Theme;

@SuppressWarnings("serial")
class ExileViewer extends CardListViewer {
    ExileViewer(final SwingGameController controller, final boolean opponent) {
        super(
            controller,
            controller.getViewerInfo().getPlayerInfo(opponent).exile,
            "Exile : " + controller.getViewerInfo().getPlayerInfo(opponent).getName(),
            Theme.ICON_SMALL_EXILE
        );

        update();
    }
}
