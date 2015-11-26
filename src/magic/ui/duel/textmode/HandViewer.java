package magic.ui.duel.textmode;

import magic.ui.duel.SwingGameController;
import magic.ui.theme.Theme;

class HandViewer extends CardListViewer {
    private static final long serialVersionUID = 1L;

    HandViewer(final SwingGameController controller) {
        super(
            controller,
            controller.getViewerInfo().getPlayerInfo(false).hand,
            "Hand : " + controller.getViewerInfo().getPlayerInfo(false).getName(),
            Theme.ICON_SMALL_HAND,
            /* showCost */ false
        );

        update();
    }
}
