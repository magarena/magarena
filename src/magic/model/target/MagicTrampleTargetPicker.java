package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicTrampleTargetPicker extends MagicTargetPicker<MagicPermanent> {

	private static final MagicTrampleTargetPicker INSTANCE=new MagicTrampleTargetPicker();

	private MagicTrampleTargetPicker() {}
	
	public static MagicTrampleTargetPicker create() {
		return INSTANCE;
	}

	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
		final long flags=permanent.getAllAbilityFlags();
		if (!MagicAbility.Trample.hasAbility(flags)&&
			!MagicAbility.Defender.hasAbility(flags)&&
			!MagicAbility.CannotAttackOrBlock.hasAbility(flags)) {
			return 1+permanent.getPower();
		}
		return 0;
	}
}
