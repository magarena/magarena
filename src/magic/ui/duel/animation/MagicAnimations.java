package magic.ui.duel.animation;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import magic.data.GeneralConfig;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.phase.MagicPhaseType;
import magic.ui.duel.CardViewerInfo;
import magic.ui.duel.DuelPanel;
import magic.ui.duel.GameViewerInfo;
import magic.utility.MagicSystem;

public class MagicAnimations {
    private MagicAnimations() {}

    private static GameLayoutInfo layoutInfo;

    public static MagicAnimation getGameplayAnimation(
        final GameViewerInfo oldGameInfo,
        final GameViewerInfo newGameInfo,
        final DuelPanel gamePanel) {

        if (AnimationFx.isOn(AnimationFx.DRAW_CARD) && isDrawCardEvent(newGameInfo)) {
            return getDrawCardAnimation(oldGameInfo, newGameInfo, gamePanel);
        }

        if (AnimationFx.isOn(AnimationFx.PLAY_CARD) && isPlayCardEvent(newGameInfo)) {
            return getPlayCardAnimationInfo(oldGameInfo, newGameInfo, gamePanel);
        }

        if (AnimationFx.isOn(AnimationFx.NEW_TURN_MSG) && isNewTurnEvent(oldGameInfo, newGameInfo)) {
            return getNewTurnAnimation(newGameInfo, gamePanel);
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

    private static MagicAnimation getNewTurnAnimation(GameViewerInfo newGameInfo, DuelPanel gamePanel) {
        setLayoutInfo(gamePanel, newGameInfo, MagicCardDefinition.UNKNOWN);
        return new NewTurnAnimation(newGameInfo, layoutInfo);
    }

    private static void setLayoutInfo(DuelPanel gamePanel, GameViewerInfo newGameInfo, MagicCardDefinition aCard) {
        assert !SwingUtilities.isEventDispatchThread();
        try {
            SwingUtilities.invokeAndWait(() -> {
                layoutInfo = gamePanel.getLayoutInfo(newGameInfo, aCard);
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
        final MagicCardDefinition aCard = cards.get(0).getCardDefinition();

        setLayoutInfo(gamePanel, newGameInfo, aCard);

        return new DrawCardAnimation(
            newGameInfo.getTurnPlayer().player,
            aCard,
            layoutInfo
        );
    }

    /**
     * AI plays card from hand during M1 or M2.
     */
    private static MagicAnimation getPlayCardAnimationInfo(
        final GameViewerInfo oldGameInfo,
        final GameViewerInfo newGameInfo,
        final DuelPanel gamePanel) {

        // if a card has been played then the current game state's hand will have one
        // less card than the previous game state's hand.
        final List<MagicCard> cards = new ArrayList<>(oldGameInfo.getTurnPlayer().hand);
        cards.removeAll(newGameInfo.getTurnPlayer().hand);
        if (cards.isEmpty()) {
            return null;
        }
        assert cards.size() == 1;

        final CardViewerInfo cardInfo = newGameInfo.getCardViewerInfo(cards.get(0));

        setLayoutInfo(gamePanel, newGameInfo, cardInfo.getFaceupCardDef());

        return new PlayCardAnimation(
            newGameInfo.getTurnPlayer().player,
            cardInfo.getFaceupCardDef(),
            layoutInfo
        );

    }

    public static void debugPrint(MagicAnimation animation) {
        if (animation != null) {
            if (animation instanceof CardAnimation) {
                final CardAnimation ca = (CardAnimation)animation;
                System.out.printf("\n%s %s %s\n",
                    ca.getPlayer().getName(),
                    ca instanceof DrawCardAnimation ? "draws" : "plays",
                    ca.getCard()
                );
            }
        }
    }
  
}
