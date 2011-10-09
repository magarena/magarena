package magic.model.phase;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.action.MagicDestroyAction;
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
            final MagicPermanentList toBeExiled = new MagicPermanentList();
            final MagicPermanentList toBeDestroyed = new MagicPermanentList();
            for (final MagicPermanent permanent : player.getPermanents()) {
                if (permanent.hasState(MagicPermanentState.ExileAtEndOfCombat)) {
                    toBeExiled.add(permanent);
                } else if (permanent.hasState(MagicPermanentState.DestroyAtEndOfCombat)) {
                	toBeDestroyed.add(permanent);
                } else if (permanent.isAttacking()||permanent.isBlocking()) {
                    game.doAction(new MagicRemoveFromCombatAction(permanent));
                }
            }
            for (final MagicPermanent permanent : toBeExiled) {
                game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.Exile));
			}
            for (final MagicPermanent permanent : toBeDestroyed) {
            	game.doAction(new MagicDestroyAction(permanent));
			}
		}
	}
}
