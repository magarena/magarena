package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicPowerTargetPicker extends MagicTargetPicker<MagicPermanent> {

	private static final MagicPowerTargetPicker INSTANCE = new MagicPowerTargetPicker();
	
	private MagicPowerTargetPicker() {}
	
	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
		return permanent.getPower(game);
	}
	
	public static MagicPowerTargetPicker getInstance() {
		return INSTANCE;
	}
}
