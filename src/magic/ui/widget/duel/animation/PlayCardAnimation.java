package magic.ui.widget.duel.animation;

import java.awt.Rectangle;
import magic.model.MagicPlayer;
import magic.ui.duel.viewer.info.CardViewerInfo;

/**
 * Animation when playing a card from hand.
 */
public class PlayCardAnimation extends CardAnimation {

    public PlayCardAnimation(MagicPlayer aPlayer, CardViewerInfo cardInfo, GameLayoutInfo layoutInfo) {
        super(aPlayer, cardInfo, layoutInfo);
    }

    @Override
    protected Rectangle getStart() {
        return getLayoutInfo().getHandButtonLayout(getPlayerIndex());
    }

    @Override
    protected Rectangle getEnd() {
        return getCardInfo().usesStack()
            ? getLayoutInfo().getStackLayout()
            : getLayoutInfo().getPermanentsZoneLayout(getPlayerIndex());
    }

//    /**
//     *  Draws a one pixel border of choiceColor.
//     */
//    private void drawBorderHighlight(Graphics g) {
//        final Graphics2D g2d=(Graphics2D)g;
//        g2d.setPaint(Color.RED);
//        int strokeWidth = 4;
//        g2d.setStroke(new BasicStroke(strokeWidth));
//        strokeWidth = strokeWidth / 2;
//        g2d.drawRect(
//                startRect.x + strokeWidth,
//                startRect.y + strokeWidth,
//                startRect.width - strokeWidth,
//                startRect.height - strokeWidth);
//    }

//    /**
//     *  Draws a transparent overlay of choiceColor.
//     */
//    private void drawTransparentOverlay(final Graphics g) {
//        final Color choiceColor = ThemeFactory.getInstance().getCurrentTheme().getChoiceColor();
//        final Graphics2D g2d = (Graphics2D)g;
//        g2d.setPaint(choiceColor);
//        g2d.fillRect(
//                startRect.x - 1,
//                startRect.y - 1,
//                startRect.width + 2,
//                startRect.height + 2);
//    }

//    /**
//     * If player Hand is visible then start animation from position of the card
//     * in the Hand otherwise start animation from the Hand icon next to player portrait.
//     */
//    private void setAnimationStartPoint(final PlayCardAnimationInfo animation, final MagicPlayer player, final MagicCardDefinition card) {
//        if (isPlayerHandVisible(player)) {
//            final ImageCardListViewer handViewer = controller.getPlayerZoneViewer().getImageCardsListViewer();
//            final Point startPoint = handViewer.getCardPosition(card);
//            animation.setStartSize(handViewer.getCardSize());
//            animation.setStartPoint(startPoint);
//        }
//    }
//
//    /**
//     * TODO: checking the player index to determine if the Hand is visible is
//     * not really desirable but it works. The index is a bad code smell. It is
//     * used to position the player on screen and determine whether a player
//     * is human or AI (except for an AI v AI game). It seems very arbitrary.
//     */
//    private boolean isPlayerHandVisible(final MagicPlayer player) {
//        return player.getIndex() == 0;
//    }

}
