package magic.model.choice;

import java.util.ArrayList;
import java.util.Arrays;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicMappable;

public class MagicCardChoiceResult extends ArrayList<MagicCard> implements MagicMappable {
		
	private static final long serialVersionUID = 1L;

	public MagicCardChoiceResult(final MagicCard cards[]) {
		addAll(Arrays.asList(cards));
	}
	
	public MagicCardChoiceResult() {}
	
	@Override
	public Object map(final MagicGame game) {
		final MagicCardChoiceResult result=new MagicCardChoiceResult();
		for (final MagicCard card : this) {
			result.add((MagicCard)card.map(game));
		}
		return result;
	}

	@Override
	public String toString() {
		final StringBuffer buffer=new StringBuffer();
		for (final MagicCard card : this)  {
			if (buffer.length()>0) {
				buffer.append(", ");
			}
			buffer.append(card.getName());
		}
		return buffer.toString();
	}

    @Override
    public long getId() {
        int idx = 0;
		long[] keys = new long[size() + 1];
		for (final MagicCard card : this) {
			keys[idx] = card.getCardDefinition().getIndex();
            idx++;
		}
		return magic.MurmurHash3.hash(keys);
    }
}
