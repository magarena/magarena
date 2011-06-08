package magic.model.phase;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeExtraTurnsAction;
import magic.model.action.MagicCleanupPlayerAction;
import magic.model.action.MagicCleanupTurnTriggersAction;
import magic.model.action.MagicPayDelayedCostsAction;

public class MagicCleanupPhase extends MagicPhase {

	private static final MagicPhase INSTANCE=new MagicCleanupPhase();
	
	private MagicCleanupPhase() {
		
		super(MagicPhaseType.Cleanup);	
	}
	
	public static MagicPhase getInstance() {
		
		return INSTANCE;
	}
	
	private void cleanup(final MagicGame game) {

		game.doAction(new MagicCleanupTurnTriggersAction());
		for (final MagicPlayer player : game.getPlayers()) {
			
			game.doAction(new MagicCleanupPlayerAction(player));
		}
	}
	
	private void nextTurn(final MagicGame game) {

		MagicPlayer turnPlayer=game.getTurnPlayer();
		if (turnPlayer.getExtraTurns()>0) {
			game.doAction(new MagicChangeExtraTurnsAction(turnPlayer,-1));
			game.logMessage(turnPlayer,"You take an extra turn.");
		} else {
			turnPlayer=game.getOpponent(turnPlayer);
			game.setTurnPlayer(turnPlayer);
		}
		if (!turnPlayer.getBuilderCost().isEmpty()) {
			game.doAction(new MagicPayDelayedCostsAction(turnPlayer));
		}
		game.setTurn(game.getTurn()+1);
		game.resetLandPlayed();
	}
	
	@Override
	public void executeBeginStep(final MagicGame game) {

		cleanup(game);
		nextTurn(game);
		game.setStep(MagicStep.NextPhase);
	}
}
