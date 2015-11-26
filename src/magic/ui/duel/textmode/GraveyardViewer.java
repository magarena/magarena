package magic.ui.duel.textmode;

import magic.ui.duel.SwingGameController;
import magic.ui.theme.Theme;

class GraveyardViewer extends CardListViewer {
    private static final long serialVersionUID = 1L;

    GraveyardViewer(final SwingGameController controller, final boolean opponent) {
        super(
            controller,
            controller.getViewerInfo().getPlayerInfo(opponent).graveyard,
            "Graveyard : " + controller.getViewerInfo().getPlayerInfo(opponent).getName(),
            Theme.ICON_SMALL_GRAVEYARD
        );

        update();
    }
}
