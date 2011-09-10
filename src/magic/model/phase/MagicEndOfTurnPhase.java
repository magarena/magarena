package magic.model.phase;

import magic.data.SoundEffects;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicReturnExiledAction;
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
		
		// Exiled until end of turn.
		game.doAction(new MagicReturnExiledAction());
		
		// End of turn triggers.
		game.executeTrigger(MagicTriggerType.AtEndOfTurn,game.getTurnPlayer());
		game.setStep(MagicStep.ActivePlayer);
	}	
	
	@Override
	protected void executeEndOfPhase(final MagicGame game) {
		SoundEffects.playClip(game,SoundEffects.TURN_SOUND);
	}
}
