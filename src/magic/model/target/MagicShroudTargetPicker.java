package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicShroudTargetPicker extends MagicTargetPicker {

	private static final MagicTargetPicker INSTANCE=new MagicShroudTargetPicker();
	
	private MagicShroudTargetPicker() {
		
	}
	
	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final Object target) {

		final MagicPermanent permanent=(MagicPermanent)target;
		final long flags=permanent.getAllAbilityFlags(game);
		if (MagicAbility.Shroud.hasAbility(flags)||MagicAbility.CannotBeTheTarget.hasAbility(flags)) {
			return 0;
		}
		return permanent.getScore(game);
	}
	
	public static MagicTargetPicker getInstance() {
		
		return INSTANCE;
	}
}