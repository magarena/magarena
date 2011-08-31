package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicLifelinkTargetPicker extends MagicTargetPicker {

	private static final MagicTargetPicker INSTANCE = new MagicLifelinkTargetPicker();
	
	private static final int ATTACKING_UNBLOCKED = 5<<8;
	private static final int ATTACKING = 4<<8;
	private static final int BLOCKING = 3<<8;
	private static final int CAN_TAP = 2<<8;
	private static final int DOUBLE_STRIKE = 1<<8;
	
	private MagicLifelinkTargetPicker() {
	}

	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final Object target) {

		final MagicPermanent permanent=(MagicPermanent)target;
		final long flags = permanent.getAllAbilityFlags(game);
		int score = 0;

		if (MagicAbility.LifeLink.hasAbility(flags) ||
			MagicAbility.CannotAttackOrBlock.hasAbility(flags)) {
			return 0;
		}
		
		if (permanent.isAttacking()) {
			if (permanent.isBlocked()) {
				score = ATTACKING;
			} else {
				score = ATTACKING_UNBLOCKED;
			}
		} else if (permanent.isBlocking()) {
			score = BLOCKING;
		} else if (permanent.canTap(game)) {
			score = CAN_TAP;
		} 

		if (MagicAbility.DoubleStrike.hasAbility(flags)) {
			score += DOUBLE_STRIKE;
		}
		
		return permanent.getPower(game) + score;
	}
	
	public static MagicTargetPicker getInstance() {
		return INSTANCE;
	}
}