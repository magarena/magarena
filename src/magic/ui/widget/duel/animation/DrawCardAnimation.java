package magic.ui.widget.duel.animation;

import java.awt.Rectangle;
import magic.model.MagicPlayer;
import magic.ui.duel.viewer.info.CardViewerInfo;

/**
 * Animation when drawing a card from library to hand.
 */
public class DrawCardAnimation extends CardAnimation {

    public DrawCardAnimation(MagicPlayer aPlayer, CardViewerInfo cardInfo, GameLayoutInfo layoutInfo) {
        super(aPlayer, cardInfo, layoutInfo);
    }

    @Override
    protected Rectangle getStart() {
        return getLayoutInfo().getLibraryButtonLayout(getPlayerIndex());
    }

    @Override
    protected Rectangle getEnd() {
        return getLayoutInfo().getHandButtonLayout(getPlayerIndex());
    }

}
