package magic.ui.duel.textmode;

import magic.model.MagicCardList;
import magic.ui.duel.SwingGameController;
import magic.ui.duel.BattlefieldPanel;
import magic.ui.duel.resolution.ResolutionProfileResult;
import magic.ui.duel.resolution.ResolutionProfileType;
import magic.ui.duel.sidebar.StackViewer;
import magic.ui.duel.viewer.info.CardViewerInfo;

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
    public StackViewer getStackViewer() {
        return null;
    }

    @Override
    public void highlightCard(CardViewerInfo cardInfo, boolean b) {
        // not supported
    }

}
