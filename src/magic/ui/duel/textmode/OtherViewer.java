package magic.ui.duel.textmode;

import magic.model.MagicCardList;
import magic.ui.duel.SwingGameController;
import magic.ui.theme.Theme;

@SuppressWarnings("serial")
class OtherViewer extends CardListViewer {
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
