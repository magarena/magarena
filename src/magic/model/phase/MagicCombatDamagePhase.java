package magic.model.phase;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.action.MagicCombatDamageAction;

public class MagicCombatDamagePhase extends MagicPhase {

	private static final MagicPhase INSTANCE=new MagicCombatDamagePhase();

	private MagicCombatDamagePhase() {
		
		super(MagicPhaseType.CombatDamage);
	}

	public static MagicPhase getInstance() {
		
		return INSTANCE;
	}

	@Override
	public void executeBeginStep(final MagicGame game) {

		final MagicPlayer attackingPlayer=game.getTurnPlayer();
		final MagicPlayer defendingPlayer=game.getOpponent(attackingPlayer);
		final int lifeBefore=defendingPlayer.getLife();
		game.doAction(new MagicCombatDamageAction(attackingPlayer,defendingPlayer));
		final int lifeAfter=defendingPlayer.getLife();
		if (lifeAfter>lifeBefore) {
			game.logMessage(defendingPlayer,"{c} You gain "+(lifeAfter-lifeBefore)+" life.");
		} else if (lifeAfter<lifeBefore) {
			game.logMessage(defendingPlayer,"{c} You lose "+(lifeBefore-lifeAfter)+" life.");
		}
		game.setStep(MagicStep.NextPhase);
	}	
}