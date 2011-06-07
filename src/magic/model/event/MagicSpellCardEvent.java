package magic.model.event;

import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;

public abstract class MagicSpellCardEvent implements MagicCardEvent,MagicEventAction {

	private MagicCardDefinition card;

    public MagicSpellCardEvent(final String name) {
	    card=CardDefinitions.getInstance().getCard(name);
	}

    public void setCardDefinition(final MagicCardDefinition card) {
        this.card = card;
    }
	
	public final MagicCardDefinition getCardDefinition() {
		
		return card;
	}
}
