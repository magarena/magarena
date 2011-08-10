package magic.model.choice;

import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.ui.GameController;

import java.util.Collection;
import java.util.Collections;

public class MagicRandomCardChoice extends MagicChoice {

	private final int amount;
	
	public MagicRandomCardChoice(final int amount) {
		super("");
		this.amount=amount;
	}

	/** Seed the rng with source id + player id */
    private MagicCardChoiceResult discard(final MagicPlayer player, final MagicSource source) {
		final MagicCardChoiceResult result=new MagicCardChoiceResult();
		final MagicCardList hand=new MagicCardList(player.getHand());
        final magic.MersenneTwisterFast rng = new magic.MersenneTwisterFast(source.getId() + player.getId());
		int actualAmount=Math.min(hand.size(),amount);
		for (;actualAmount>0;actualAmount--) {
            final int index = rng.nextInt(hand.size());
			result.add(hand.remove(index));
		}
        return result;
    }

	@Override
	public Collection<Object> getArtificialOptions(
            final MagicGame game,
            final MagicEvent event,
            final MagicPlayer player,
            final MagicSource source) {
		return Collections.<Object>singletonList(discard(player, source));
	}

	@Override
	public Object[] getPlayerChoiceResults(
            final GameController controller,
            final MagicGame game,
            final MagicPlayer player,
            final MagicSource source) {
		return new Object[]{discard(player, source)};
	}
}
