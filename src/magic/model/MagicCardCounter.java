package magic.model;

import java.util.Arrays;

import magic.data.CardDefinitions;

public class MagicCardCounter {

	private final int counts[];
	
	public MagicCardCounter() {
		counts=new int[CardDefinitions.getInstance().getNumberOfCards()];
	}	
	
	public MagicCardCounter(final MagicCardCounter cardCounter) {
		counts=Arrays.copyOf(cardCounter.counts,cardCounter.counts.length);
	}
	
	public void incrementCount(final MagicCardDefinition cardDefinition) {
		counts[cardDefinition.getIndex()]++;
	}
	
	public void decrementCount(final MagicCardDefinition cardDefinition) {
		counts[cardDefinition.getIndex()]--;
	}
	
	public int getCount(final MagicCardDefinition cardDefinition) {
		return counts[cardDefinition.getIndex()];
	}
	
	public int getCount(final int cardDefinitionIndex) {
		return counts[cardDefinitionIndex];
	}
}
