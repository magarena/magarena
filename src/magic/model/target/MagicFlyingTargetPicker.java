package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicFlyingTargetPicker extends MagicTargetPicker<MagicPermanent> {

	private static final MagicFlyingTargetPicker INSTANCE=new MagicFlyingTargetPicker();

	private MagicFlyingTargetPicker() {}
	
	public static MagicFlyingTargetPicker create() {
		return INSTANCE;
	}

	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
		final long flags=permanent.getAllAbilityFlags(game);
		if (MagicAbility.CannotAttackOrBlock.hasAbility(flags)) {
			return 0;
		}
		final int power=permanent.getPower(game);
		if (MagicAbility.Flying.hasAbility(flags)) {
			return power;
		} 
		if (MagicAbility.Defender.hasAbility(flags)) {
			return 20+power;
		}
		if (MagicAbility.Reach.hasAbility(flags)) {
			return 50+power;
		}
		return 100+power;
	}
}
