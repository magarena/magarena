package magic.model.phase;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeExtraTurnsAction;
import magic.model.action.MagicCleanupPlayerAction;
import magic.model.action.MagicCleanupTurnTriggersAction;
import magic.model.action.MagicCleanupTurnStaticsAction;
import magic.model.action.MagicPayDelayedCostsAction;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;

public class MagicCleanupPhase extends MagicPhase {

    private static final MagicPhase INSTANCE=new MagicCleanupPhase();
    
    private MagicCleanupPhase() {
        super(MagicPhaseType.Cleanup);    
    }
    
    public static MagicPhase getInstance() {
        return INSTANCE;
    }
    
    private static void cleanup(final MagicGame game) {
        game.doAction(new MagicCleanupTurnTriggersAction());
        for (final MagicPlayer player : game.getPlayers()) {
            game.doAction(new MagicCleanupPlayerAction(player));
        }
        game.doAction(new MagicCleanupTurnStaticsAction());
        game.checkState();
    }
    
    private static void nextTurn(final MagicGame game) {
        MagicPlayer turnPlayer=game.getTurnPlayer();
        // discard down to 7 cards
        if (turnPlayer.getHandSize() > 7) {
            final int amount = turnPlayer.getHandSize() - 7;
            game.addEvent(new MagicDiscardEvent(MagicEvent.NO_SOURCE,turnPlayer,amount,false));
        }
        if (turnPlayer.getExtraTurns()>0) {
            game.doAction(new MagicChangeExtraTurnsAction(turnPlayer,-1));
            final String playerName = turnPlayer.getName();
            game.logMessage(turnPlayer,playerName + " takes an extra turn.");
        } else {
            turnPlayer=turnPlayer.getOpponent();
            game.setTurnPlayer(turnPlayer);
        }
        if (!turnPlayer.getBuilderCost().isEmpty()) {
            game.doAction(new MagicPayDelayedCostsAction(turnPlayer));
        }
        game.setTurn(game.getTurn()+1);
        game.resetLandPlayed();
        game.setCreatureDiedThisTurn(false);
        game.setSpellsPlayed(0);
    }
    
    @Override
    public void executeBeginStep(final MagicGame game) {
        cleanup(game);
        nextTurn(game);
        game.setStep(MagicStep.NextPhase);
    }
}
