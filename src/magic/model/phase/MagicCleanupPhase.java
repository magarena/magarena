package magic.model.phase;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeExtraTurnsAction;
import magic.model.action.MagicCleanupPlayerAction;
import magic.model.action.MagicCleanupTurnStaticsAction;
import magic.model.action.MagicCleanupTurnTriggersAction;
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
        final MagicPlayer turnPlayer=game.getTurnPlayer();
        // discard excess cards
        if (turnPlayer.getNumExcessCards() > 0) {
            final int amount = turnPlayer.getNumExcessCards();
            game.addEvent(new MagicDiscardEvent(MagicEvent.NO_SOURCE,turnPlayer,amount));
        }
        // remove until EOT triggers/static, clean up player and permanents
        game.doAction(new MagicCleanupTurnTriggersAction());
        for (final MagicPlayer player : game.getPlayers()) {
            game.doAction(new MagicCleanupPlayerAction(player));
        }
        game.doAction(new MagicCleanupTurnStaticsAction());
        game.checkState();
    }

    private static void nextTurn(final MagicGame game) {
        final MagicPlayer turnPlayer=game.getTurnPlayer();
        if (!turnPlayer.getBuilderCost().isEmpty()) {
            game.doAction(new MagicPayDelayedCostsAction(turnPlayer));
        }
        if (turnPlayer.getExtraTurns()>0) {
            game.doAction(new MagicChangeExtraTurnsAction(turnPlayer,-1));
            final String playerName = turnPlayer.getName();
            game.logMessage(turnPlayer,playerName + " takes an extra turn.");
        } else {
            game.setTurnPlayer(turnPlayer.getOpponent());
        }
        game.setTurn(game.getTurn()+1);
        game.resetLandPlayed();
        game.resetMaxLand();
        game.setCreatureDiedThisTurn(false);
        game.setSpellsPlayedLastTurn(game.getSpellsPlayed());
        game.setSpellsPlayed(0);
    }

    @Override
    public void executeBeginStep(final MagicGame game) {
        cleanup(game);
        if (game.getStack().isEmpty()) {
            game.setStep(MagicStep.NextPhase);
        } else {
            game.setStep(MagicStep.ActivePlayer);
        }
    }
    
    @Override
    public void executeEndOfPhase(final MagicGame game) {
        nextTurn(game);
    }
}
