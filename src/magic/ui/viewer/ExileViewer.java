package magic.ui.viewer;

import magic.model.MagicCardList;
import magic.ui.GameController;
import magic.ui.theme.Theme;

public class ExileViewer extends CardListViewer {
    private static final long serialVersionUID = 1L;

    public ExileViewer(final ViewerInfo viewerInfo, final GameController controller, final boolean opponent) {
        super(
            controller,
            viewerInfo.getPlayerInfo(opponent).exile,
            "Exile : " + viewerInfo.getPlayerInfo(opponent).name,
            Theme.ICON_SMALL_EXILE
        );

        update();
    }
}
