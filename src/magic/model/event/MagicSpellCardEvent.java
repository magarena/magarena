package magic.model.event;

import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;

public abstract class MagicSpellCardEvent implements MagicCardEvent,MagicEventAction {

	private final MagicCardDefinition card;
	
	public MagicSpellCardEvent(final String name) {
		
		card=CardDefinitions.getInstance().getCard(name);
	}
	
	public final MagicCardDefinition getCardDefinition() {
		
		return card;
	}
}