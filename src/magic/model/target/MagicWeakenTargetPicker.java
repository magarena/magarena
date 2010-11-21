package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;

public class MagicWeakenTargetPicker extends MagicTargetPicker {

	private static final int ATTACKING_BLOCKED=4<<8;
	private static final int ATTACKING=3<<8;
	private static final int BLOCKING=2<<8;
	private static final int CAN_TAP=1<<8;
		
	private final int amountToughness;
	
	public MagicWeakenTargetPicker(final int amountPower,final int amountToughness) {
		
		this.amountToughness=amountToughness;
	}

	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final Object target) {

		final MagicPermanent permanent=(MagicPermanent)target;
		final MagicPowerToughness pt=permanent.getPowerToughness(game);

		final int lethalToughness=pt.getPositiveToughness()-permanent.getDamage();
		if (lethalToughness<=amountToughness) {
			return permanent.getScore(game)<<12;
		}		

		int score=0;
		
		// First level.
		if (permanent.isAttacking()) {
			if (permanent.isBlocked()) {
				score=ATTACKING_BLOCKED;
			} else {
				score=ATTACKING;
			}
		} else if (permanent.isBlocking()) {
			score=BLOCKING;
		} else if (permanent.canTap(game)) {
			score=CAN_TAP;
		}

		if (amountToughness>0) {
			// Second level.
			score+=Math.max(15,lethalToughness)<<4;
		}
		
		// Third level.
		score+=Math.max(15,pt.getPositivePower());

		return score;
	}
}