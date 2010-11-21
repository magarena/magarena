package magic.model.phase;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.trigger.MagicTriggerType;

public class MagicEndOfTurnPhase extends MagicPhase {

	private static final MagicPhase INSTANCE=new MagicEndOfTurnPhase();
	
	private MagicEndOfTurnPhase() {
		
		super(MagicPhaseType.EndOfTurn);	
	}
	
	public static MagicPhase getInstance() {
		
		return INSTANCE;
	}
	
	@Override
	public void executeBeginStep(final MagicGame game) {

		// Remove from game or sacrifice at end of turn.
		for (final MagicPlayer player : game.getPlayers()) {

			boolean changed;
			do {
				changed=false;
				for (final MagicPermanent permanent : player.getPermanents()) {
				
					if (permanent.endOfTurn(game)) {
						changed=true;
						break;
					}
				}
			} while (changed);
		}
		
		// End of turn triggers.
		game.executeTrigger(MagicTriggerType.AtEndOfTurn,game.getTurnPlayer());
		game.setStep(MagicStep.ActivePlayer);
	}	
}