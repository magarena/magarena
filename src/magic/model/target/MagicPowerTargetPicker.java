package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicPowerTargetPicker extends MagicTargetPicker {

	private static final MagicTargetPicker INSTANCE=new MagicPowerTargetPicker();
	
	private MagicPowerTargetPicker() {
		
	}
	
	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final Object target) {

		final MagicPermanent permanent=(MagicPermanent)target;
		return permanent.getPower(game);
	}
	
	public static MagicTargetPicker getInstance() {
		
		return INSTANCE;
	}
}