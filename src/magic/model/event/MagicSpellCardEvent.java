package magic.model.event;

import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;

public abstract class MagicSpellCardEvent implements MagicCardEvent,MagicEventAction {

	private int cardIndex;
    
    public MagicSpellCardEvent() {}

    public MagicSpellCardEvent(final String name) {
	    cardIndex = CardDefinitions.getInstance().getCard(name).getIndex();
	}

    public void setCardIndex(final int cardIndex) {
        this.cardIndex = cardIndex;
    }
	
	public final MagicCardDefinition getCardDefinition() {
		return CardDefinitions.getInstance().getCard(cardIndex);
	}
}
