package magic.model.phase;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.action.MagicRemoveFromCombatAction;
import magic.model.action.MagicRemoveFromPlayAction;

public class MagicEndOfCombatPhase extends MagicPhase {

	private static final MagicPhase INSTANCE=new MagicEndOfCombatPhase();
	
	private MagicEndOfCombatPhase() {
		
		super(MagicPhaseType.EndOfCombat);	
	}
	
	public static MagicPhase getInstance() {
		
		return INSTANCE;
	}
	
	@Override
	public void executeBeginStep(final MagicGame game) {

		game.setStep(game.canSkip()?MagicStep.NextPhase:MagicStep.ActivePlayer);
	}

	@Override
	public void executeEndOfPhase(final MagicGame game) {

		for (final MagicPlayer player : game.getPlayers()) {
			boolean changed;
			do {
				changed = false;
				for (final MagicPermanent permanent : player.getPermanents()) {
					if (permanent.hasState(MagicPermanentState.ExileAtEndOfCombat)) {
						game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.Exile));
						changed = true;
						break;
					}
					else if (permanent.isAttacking()||permanent.isBlocking()) {
						game.doAction(new MagicRemoveFromCombatAction(permanent));
					}
				}
			} while (changed);
		}
	}
}