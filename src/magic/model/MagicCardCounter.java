package magic.model;

import magic.data.CardDefinitions;

import java.util.Arrays;

public class MagicCardCounter {

    private final int counts[];
    
    MagicCardCounter() {
        counts=new int[CardDefinitions.getNumberOfCards()];
    }    
    
    MagicCardCounter(final MagicCardCounter cardCounter) {
        counts=Arrays.copyOf(cardCounter.counts,cardCounter.counts.length);
    }
    
    void incrementCount(final MagicCardDefinition cardDefinition) {
        counts[cardDefinition.getIndex()]++;
    }
    
    void decrementCount(final MagicCardDefinition cardDefinition) {
        counts[cardDefinition.getIndex()]--;
    }
    
    private int getCount(final MagicCardDefinition cardDefinition) {
        return counts[cardDefinition.getIndex()];
    }
    
    int getCount(final int cardDefinitionIndex) {
        return counts[cardDefinitionIndex];
    }
}
