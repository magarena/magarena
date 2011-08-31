package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicDeathtouchTargetPicker extends MagicTargetPicker {

	private static final MagicTargetPicker INSTANCE = new MagicDeathtouchTargetPicker();
	
	private static final int ATTACKING = 4<<8;
	private static final int BLOCKING = 3<<8;
	private static final int CAN_TAP = 2<<8;
	private static final int FIRST_STRIKE = 1<<8;
	
	private MagicDeathtouchTargetPicker() {
	}

	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final Object target) {

		final MagicPermanent permanent=(MagicPermanent)target;
		final long flags = permanent.getAllAbilityFlags(game);
		int score = 0;

		if (MagicAbility.Deathtouch.hasAbility(flags) ||
			MagicAbility.CannotAttackOrBlock.hasAbility(flags)) {
			return 0;
		}
		
		
		if (permanent.isBlocked()) {
			score = ATTACKING;
		} else if (permanent.isBlocking()) {
			score = BLOCKING;
		} else if (permanent.canTap(game)) {
			score = CAN_TAP;
		} 

		if (MagicAbility.FirstStrike.hasAbility(flags) ||
			MagicAbility.DoubleStrike.hasAbility(flags)) {
			score += FIRST_STRIKE;
		}
		
		return permanent.getPower(game) + score;
	}
	
	public static MagicTargetPicker getInstance() {
		return INSTANCE;
	}
}