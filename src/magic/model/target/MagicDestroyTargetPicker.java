package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicDestroyTargetPicker extends MagicTargetPicker {

	private final boolean noRegeneration;
	
	public MagicDestroyTargetPicker(final boolean noRegeneration) {
		
		this.noRegeneration=noRegeneration;
	}

	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final Object target) {

		final MagicPermanent permanent=(MagicPermanent)target;
		if (permanent.hasAbility(game,MagicAbility.Indestructible)) {
			return 0;
		}
		if (permanent.isRegenerated()&&!noRegeneration) {
			return 0;
		}
		return permanent.getScore(game);
	}
}