package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicCopyTargetPicker extends MagicTargetPicker<MagicPermanent> {

	private static final MagicTargetPicker INSTANCE=new MagicCopyTargetPicker();

	private MagicCopyTargetPicker() {}
	
	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
		return permanent.getCardDefinition().getScore();
	}

	public static MagicTargetPicker getInstance() {
		return INSTANCE;
	}
}
