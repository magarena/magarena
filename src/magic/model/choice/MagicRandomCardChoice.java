package magic.model.choice;

import java.util.Collection;
import java.util.Collections;

import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicRandom;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.ui.GameController;

public class MagicRandomCardChoice extends MagicChoice {

	private final int amount;
	
	public MagicRandomCardChoice(final int amount) {
		
		super("");
		this.amount=amount;
	}

	/** Always pick the first cards of hand in AI to create determinism. */
	@Override
	public Collection<Object> getArtificialOptions(final MagicGame game,final MagicEvent event,final MagicPlayer player,final MagicSource source) {

		final MagicCardChoiceResult result=new MagicCardChoiceResult();
		final MagicCardList hand=player.getHand();
		final int actualAmount=Math.min(hand.size(),amount);
		for (int index=0;index<actualAmount;index++) {
			
			result.add(hand.get(index));
		}
		return Collections.<Object>singletonList(result);
	}

	@Override
	public Object[] getPlayerChoiceResults(final GameController controller,final MagicGame game,final MagicPlayer player,final MagicSource source) {

		final MagicCardChoiceResult result=new MagicCardChoiceResult();
		final MagicCardList hand=new MagicCardList(player.getHand());
		int actualAmount=Math.min(hand.size(),amount);
		for (;actualAmount>0;actualAmount--) {
			
			final int index=MagicRandom.nextInt(hand.size());
			result.add(hand.remove(index));
		}
		return new Object[]{result};
	}
}