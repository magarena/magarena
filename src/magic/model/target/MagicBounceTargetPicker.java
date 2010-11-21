package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.stack.MagicCardOnStack;

/** Creatures or spells from both players. */
public class MagicBounceTargetPicker extends MagicTargetPicker {

	private static final MagicTargetPicker INSTANCE=new MagicBounceTargetPicker();
	
	private MagicBounceTargetPicker() {
		
	}
	
	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final Object target) {

		if (target instanceof MagicPermanent) {
			final MagicPermanent permanent=(MagicPermanent)target;
			int score=permanent.getScore(game);
			if (permanent.getCardDefinition().getComeIntoPlayTriggers().size()>0) {
				score-=1000;
			}
			return permanent.getController()==player?-score:score;
		}

		final MagicCardOnStack cardOnStack=(MagicCardOnStack)target;
		final int converted=1+cardOnStack.getCardDefinition().getConvertedCost();
		return cardOnStack.getController()==player?-converted:converted;
	}
	
	public static MagicTargetPicker getInstance() {
		
		return INSTANCE;
	}
}