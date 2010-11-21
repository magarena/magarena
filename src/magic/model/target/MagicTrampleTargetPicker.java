package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicTrampleTargetPicker extends MagicTargetPicker {

	private static final MagicTargetPicker INSTANCE=new MagicTrampleTargetPicker();
	
	private MagicTrampleTargetPicker() {
		
	}

	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final Object target) {

		final MagicPermanent permanent=(MagicPermanent)target;
		final long flags=permanent.getAllAbilityFlags(game);
		if (!MagicAbility.Trample.hasAbility(flags)&&
			!MagicAbility.Defender.hasAbility(flags)&&
			!MagicAbility.CannotAttackOrBlock.hasAbility(flags)) {
			return 1+permanent.getPower(game);
		}
		return 0;
	}
	
	public static MagicTargetPicker getInstance() {
		
		return INSTANCE;
	}
}