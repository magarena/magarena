package magic.ui.duel.animation;

import java.awt.Rectangle;
import magic.model.MagicCardDefinition;
import magic.model.MagicPlayer;

/**
 * Animation when drawing a card from library to hand.
 */
public class DrawCardAnimation extends CardAnimation {

    public DrawCardAnimation(MagicPlayer aPlayer, MagicCardDefinition aCard, GameLayoutInfo layoutInfo) {
        super(aPlayer, aCard, layoutInfo);
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
