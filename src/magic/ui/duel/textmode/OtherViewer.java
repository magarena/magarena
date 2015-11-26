package magic.ui.duel.textmode;

import magic.model.MagicCardList;
import magic.ui.duel.SwingGameController;
import magic.ui.theme.Theme;

class OtherViewer extends CardListViewer {
    private static final long serialVersionUID = 1L;

    OtherViewer(final MagicCardList cards, final SwingGameController controller) {
        super(
            controller,
            cards,
            "Other : " + controller.getViewerInfo().getPlayerInfo(false).getName(),
            Theme.ICON_SMALL_HAND,
            /* showCost */ false
        );

        update();
    }
}
