package magic.ui.duel.textmode;

import magic.ui.duel.SwingGameController;
import magic.ui.theme.Theme;

@SuppressWarnings("serial")
class GraveyardViewer extends CardListViewer {
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
