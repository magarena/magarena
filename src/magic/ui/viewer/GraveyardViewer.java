package magic.ui.viewer;

import magic.model.MagicCardList;
import magic.ui.GameController;
import magic.ui.theme.Theme;

public class GraveyardViewer extends CardListViewer {
    private static final long serialVersionUID = 1L;

    public GraveyardViewer(final ViewerInfo viewerInfo, final GameController controller, final boolean opponent) {
        super(
            controller,
            viewerInfo.getPlayerInfo(opponent).graveyard,
            "Graveyard : " + viewerInfo.getPlayerInfo(opponent).name,
            Theme.ICON_SMALL_GRAVEYARD
        );

        update();
    }
}
