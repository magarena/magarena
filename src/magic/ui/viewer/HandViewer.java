package magic.ui.viewer;

import magic.model.MagicCardList;
import magic.ui.GameController;
import magic.ui.theme.Theme;

public class HandViewer extends CardListViewer {
    private static final long serialVersionUID = 1L;

    public HandViewer(final ViewerInfo viewerInfo, final GameController controller) {
        super(
            controller,
            viewerInfo.getPlayerInfo(false).hand,
            "Hand : " + viewerInfo.getPlayerInfo(false).name,
            Theme.ICON_SMALL_HAND,
            /* showCost */ false
        );

        update();
    }
}
