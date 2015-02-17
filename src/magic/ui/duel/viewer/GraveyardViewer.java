package magic.ui.duel.viewer;

import magic.ui.SwingGameController;
import magic.ui.theme.Theme;

public class GraveyardViewer extends CardListViewer {
    private static final long serialVersionUID = 1L;

    public GraveyardViewer(final SwingGameController controller, final boolean opponent) {
        super(
            controller,
            controller.getViewerInfo().getPlayerInfo(opponent).graveyard,
            "Graveyard : " + controller.getViewerInfo().getPlayerInfo(opponent).name,
            Theme.ICON_SMALL_GRAVEYARD
        );

        update();
    }
}
