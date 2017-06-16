package magic.ui.widget.duel.animation;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import magic.data.GeneralConfig;
import magic.model.MagicCard;
import magic.model.phase.MagicPhaseType;
import magic.ui.duel.viewerinfo.CardViewerInfo;
import magic.ui.duel.viewerinfo.GameViewerInfo;
import magic.ui.duel.viewerinfo.PlayerViewerInfo;
import magic.ui.screen.duel.game.DuelPanel;
import magic.utility.MagicSystem;

public class MagicAnimations {
    private MagicAnimations() {}

    private static GameLayoutInfo layoutInfo;
    private static boolean isEnabled = true;

    public static MagicAnimation getGameplayAnimation(
        final GameViewerInfo oldGameInfo,
        final GameViewerInfo newGameInfo,
        final DuelPanel gamePanel) {

        if (!isEnabled) {
            return null;
        }

        if (isOn(AnimationFx.DRAW_CARD) && isDrawCardEvent(newGameInfo)) {
            return getDrawCardAnimation(oldGameInfo, newGameInfo, gamePanel);
        }

        if (isOn(AnimationFx.PLAY_CARD) && isPlayCardEvent(newGameInfo)) {
            return getPlayCardAnimationInfo(oldGameInfo, newGameInfo, gamePanel);
        }

        if (isOn(AnimationFx.NEW_TURN_MSG) && isNewTurnEvent(oldGameInfo, newGameInfo)) {
            gamePanel.doNewTurnNotification(newGameInfo);
        }

        return null;
    }

    private static boolean isDrawCardEvent(GameViewerInfo newGameInfo) {
        final boolean isDrawPhase = newGameInfo.getPhaseType() == MagicPhaseType.Draw;
        final boolean isValid = newGameInfo.getTurnPlayer().isHuman() || MagicSystem.isAiVersusAi();
        return isDrawPhase && isValid;
    }

    private static boolean isPlayCardEvent(GameViewerInfo newGameInfo) {
        return newGameInfo.getPhaseType().isMain() && newGameInfo.getTurnPlayer().isAi();
    }

    private static boolean isNewTurnEvent(GameViewerInfo oldGameInfo, GameViewerInfo newGameInfo) {
        final int turn = newGameInfo.getTurn();
        if (turn != oldGameInfo.getTurn()) {
            final GeneralConfig config = GeneralConfig.getInstance();
            final boolean isShowingMulliganScreen = config.showMulliganScreen() && turn == 1;
            return !isShowingMulliganScreen && config.getNewTurnAlertDuration() > 0;
        }
        return false;
    }

    private static void setLayoutInfo(DuelPanel gamePanel, GameViewerInfo newGameInfo, CardViewerInfo cardInfo) {
        assert !SwingUtilities.isEventDispatchThread();
        try {
            SwingUtilities.invokeAndWait(() -> {
                layoutInfo = gamePanel.getLayoutInfo(newGameInfo, cardInfo);
            });
        } catch (InterruptedException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * A player draws a card by putting the top card of his or her library into his or her hand.
     * Instead of just appearing suddenly in the player's hand, this animation will first display
     * the card briefly. This only applies when the non-AI player draws a card.
     */
    private static MagicAnimation getDrawCardAnimation(
        final GameViewerInfo oldGameInfo,
        final GameViewerInfo newGameInfo,
        final DuelPanel gamePanel) {

        final List<MagicCard> cards = new ArrayList<>(oldGameInfo.getTurnPlayer().library);
        cards.removeAll(newGameInfo.getTurnPlayer().library);

        if (cards.isEmpty()) {
            return null;
        }

        assert cards.size() == 1;
        final CardViewerInfo cardInfo = newGameInfo.getCardViewerInfo(cards.get(0));

        setLayoutInfo(gamePanel, newGameInfo, cardInfo);

        return new DrawCardAnimation(newGameInfo.getTurnPlayer(), cardInfo, layoutInfo);
    }

    /**
     * AI plays a card from hand to the battlefield or stack during M1 or M2.
     */
    private static MagicAnimation getPlayCardAnimationInfo(
        final GameViewerInfo oldGameInfo,
        final GameViewerInfo newGameInfo,
        final DuelPanel gamePanel) {

        PlayerViewerInfo tpOld = oldGameInfo.getTurnPlayer();
        PlayerViewerInfo tpNew = newGameInfo.getTurnPlayer();

        // if a card has been played then the current game state's hand should have one
        // less card than the previous game state's hand.
        final List<MagicCard> cards = new ArrayList<>(tpOld.hand);
        cards.removeAll(tpNew.hand);

        if (cards.isEmpty()) {
            return null;
        } else if (cards.size() > 1) {
            return null; // eg. due to Tolarian Winds.
        } else if (tpNew.graveyard.size() > tpOld.graveyard.size()) {
            return null; // discarding card from hand (see github issue #695).
        }

        final CardViewerInfo cardInfo = newGameInfo.getCardViewerInfo(cards.get(0));

        setLayoutInfo(gamePanel, newGameInfo, cardInfo);

        return new PlayCardAnimation(tpNew, cardInfo, layoutInfo);
    }

    public static void debugPrint(MagicAnimation animation) {
        if (animation != null) {
            if (animation instanceof CardAnimation) {
                final CardAnimation ca = (CardAnimation)animation;
                System.out.printf("\n%s %s %s\n",
                    ca.getPlayer().getName(),
                    ca instanceof DrawCardAnimation ? "draws" : "plays",
                    ca.getCardInfo()
                );
            }
        }
    }

    public static boolean isOn(long aFlag) {
        return GeneralConfig.getInstance().showGameplayAnimations() && AnimationFx.isOn(aFlag);
    }

    public static boolean isOff(long aFlag) {
        return !GeneralConfig.getInstance().showGameplayAnimations() || !AnimationFx.isOn(aFlag);
    }

//    private static void showDebugInfo(GameViewerInfo oldGameInfo, GameViewerInfo newGameInfo, MagicCard aCard) {
//        PlayerViewerInfo tpOld = oldGameInfo.getTurnPlayer();
//        PlayerViewerInfo tpNew = newGameInfo.getTurnPlayer();
//        System.out.println("===" + aCard + "===");
//        System.out.printf("  hand %d : %d\n", tpOld.hand.size(), tpNew.hand.size());
//        System.out.printf("  graveyard %d : %d\n", tpOld.graveyard.size(), tpNew.graveyard.size());
//        System.out.printf("  permanents %d : %d\n", tpOld.permanents.size(), tpNew.permanents.size());
//        System.out.printf("  stack %d : %d\n", oldGameInfo.getStack().size(), newGameInfo.getStack().size());
//    }

    public static void setEnabled(boolean b) {
        isEnabled = b;
    }

}
