package magic.ui.duel.viewer;

import magic.ui.GameController;
import magic.ui.theme.Theme;

public class ExileViewer extends CardListViewer {
    private static final long serialVersionUID = 1L;

    public ExileViewer(final GameController controller, final boolean opponent) {
        super(
            controller,
            controller.getViewerInfo().getPlayerInfo(opponent).exile,
            "Exile : " + controller.getViewerInfo().getPlayerInfo(opponent).name,
            Theme.ICON_SMALL_EXILE
        );

        update();
    }
}
