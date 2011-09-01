package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicLifelinkTargetPicker extends MagicTargetPicker {

	private static final MagicTargetPicker INSTANCE = new MagicLifelinkTargetPicker();
	
	private static final int ATTACKING_UNBLOCKED = 5<<8;
	private static final int BLOCKED_OR_BLOCKING = 4<<8;
	private static final int CAN_TAP = 3<<8;
	private static final int DOUBLE_STRIKE = 2<<8;
	private static final int FIRST_STRIKE = 1<<8;
	
	private MagicLifelinkTargetPicker() {
	}

	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final Object target) {

		final MagicPermanent permanent = (MagicPermanent)target;
		final long flags = permanent.getAllAbilityFlags(game);
		int score = 0;

		// no score for ability overlap or not being able to deal combat damage
		if (MagicAbility.LifeLink.hasAbility(flags) ||
			MagicAbility.CannotAttackOrBlock.hasAbility(flags)) {
			return 0;
		}
		
		if (permanent.isAttacking()) {
			if (!permanent.isBlocked()) {
				// unblocked attacker has the highest chance of gaining life
				score = ATTACKING_UNBLOCKED;
			} else {
				// possible to not gain life when blocker has first strike
				score = BLOCKED_OR_BLOCKING;
			}
		} else if (permanent.isBlocking()) {
			// possible to not gain life when attacker has first strike
			score = BLOCKED_OR_BLOCKING;
		} else if (permanent.canTap(game)) {
			// can be in combat later or possibly use a damage ability
			score = CAN_TAP;
		} 

		if (MagicAbility.DoubleStrike.hasAbility(flags)) {
			// chance to deal combat damage twice
			score += DOUBLE_STRIKE;
		}
		if (MagicAbility.FirstStrike.hasAbility(flags)) {
			// higher chance to deal combat damage
			score += FIRST_STRIKE;
		}
		
		return permanent.getPower(game) + score;
	}
	
	public static MagicTargetPicker getInstance() {
		return INSTANCE;
	}
}