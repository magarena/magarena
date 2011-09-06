package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicHasteTargetPicker extends MagicTargetPicker<MagicPermanent> {

	private static final MagicTargetPicker INSTANCE=new MagicHasteTargetPicker();
	
	private MagicHasteTargetPicker() {}
	
	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
		if (permanent.isTapped()||permanent.canTap(game)) {
			return 0;
		}
		return 1+permanent.getPower(game)+permanent.getActivations().size();
	}
	
	public static MagicTargetPicker getInstance() {
		return INSTANCE;
	}
}
