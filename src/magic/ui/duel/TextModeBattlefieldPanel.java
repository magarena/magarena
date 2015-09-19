package magic.ui.duel;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.event.MagicEvent;
import magic.ui.SwingGameController;
import magic.ui.duel.animation.PlayCardAnimation;
import magic.ui.duel.resolution.ResolutionProfileResult;
import magic.ui.duel.resolution.ResolutionProfileType;
import magic.ui.duel.viewer.BattlefieldViewer;
import magic.ui.duel.viewer.HandGraveyardExileViewer;
import magic.ui.duel.viewer.StackCombatViewer;
import magic.ui.duel.sidebar.StackViewer;

@SuppressWarnings("serial")
public class TextModeBattlefieldPanel extends BattlefieldPanel {

    private final HandGraveyardExileViewer handGraveyardViewer;
    private final StackCombatViewer stackCombatViewer;
    private final BattlefieldViewer playerPermanentViewer;
    private final BattlefieldViewer opponentPermanentViewer;

    public TextModeBattlefieldPanel(final SwingGameController controller) {
        setOpaque(false);
        //
        handGraveyardViewer = new HandGraveyardExileViewer(controller);
        stackCombatViewer = new StackCombatViewer(controller);
        playerPermanentViewer = new BattlefieldViewer(controller, false);
        opponentPermanentViewer = new BattlefieldViewer(controller, true);
        //
        setLayout(null);
        add(handGraveyardViewer);
        add(stackCombatViewer);
        add(playerPermanentViewer);
        add(opponentPermanentViewer);
    }

    @Override
    public void doUpdate() {
        handGraveyardViewer.update();
        stackCombatViewer.update();
        playerPermanentViewer.update();
        opponentPermanentViewer.update();
    }

    @Override
    public void showCards(final MagicCardList cards) {
        handGraveyardViewer.showCards(cards);
        handGraveyardViewer.setSelectedTab(5);
    }

    @Override
    public void focusViewers(int handGraveyard) {
        handGraveyardViewer.setSelectedTab(handGraveyard);
    }

    @Override
    public void resizeComponents(final ResolutionProfileResult result) {
        stackCombatViewer.setBounds(result.getBoundary(ResolutionProfileType.GameStackCombatViewer));
        handGraveyardViewer.setBounds(result.getBoundary(ResolutionProfileType.GameHandGraveyardViewer));
        playerPermanentViewer.setBounds(result.getBoundary(ResolutionProfileType.GamePlayerPermanentViewer));
        opponentPermanentViewer.setBounds(result.getBoundary(ResolutionProfileType.GameOpponentPermanentViewer));
    }

    @Override
    public void setAnimationEvent(MagicEvent event, DuelPanel gamePanel) {
        // not implemented
    }

    @Override
    public StackViewer getStackViewer() {
        return null;
    }

    @Override
    public PlayCardAnimation getPlayCardFromHandAnimation() {
        return null;
    }

    @Override
    public void setPlayCardFromHandAnimation(PlayCardAnimation event) {
        // not implemented
    }

    @Override
    public void highlightCard(MagicCard card, boolean b) {
        // not supported
    }

}
