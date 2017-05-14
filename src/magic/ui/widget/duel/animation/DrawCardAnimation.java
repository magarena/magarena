package magic.ui.widget.duel.animation;

import java.awt.Rectangle;
import magic.ui.duel.viewerinfo.CardViewerInfo;
import magic.ui.duel.viewerinfo.PlayerViewerInfo;

/**
 * Animation when drawing a card from library to hand.
 */
public class DrawCardAnimation extends CardAnimation {

    public DrawCardAnimation(PlayerViewerInfo aPlayer, CardViewerInfo cardInfo, GameLayoutInfo layoutInfo) {
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
