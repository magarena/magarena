package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.stack.MagicCardOnStack;

/** Creatures or spells from both players. */
public class MagicBounceTargetPicker extends MagicTargetPicker<MagicTarget> {

	private static final MagicBounceTargetPicker INSTANCE = new MagicBounceTargetPicker();
	
	private MagicBounceTargetPicker() {}
	
	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
		if (target.isPermanent()) {
			final MagicPermanent permanent=(MagicPermanent)target;
			int score=permanent.getScore(game);
			if (permanent.getCardDefinition().getComeIntoPlayTriggers().size()>0) {
				score-=1000;
			}
			return permanent.getController()==player?-score:score;
		} else {
            //target is MagicCardOnStack
            final MagicCardOnStack cardOnStack=(MagicCardOnStack)target;
            final int converted=1+cardOnStack.getCardDefinition().getConvertedCost();
    		return cardOnStack.getController()==player?-converted:converted;
        }
	}
	
	public static MagicBounceTargetPicker getInstance() {
		return INSTANCE;
	}
}
