package magic.model.event;

import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;

public abstract class MagicSpellCardEvent implements MagicCardEvent,MagicEventAction {

	private int cardIndex;
    
    public MagicSpellCardEvent() {}

    public void setCardIndex(final int cardIndex) {
        this.cardIndex = cardIndex;
    }
	
	public final MagicCardDefinition getCardDefinition() {
		return CardDefinitions.getCard(cardIndex);
    }
	
    @Override
    public void executeEvent(
            final MagicGame game, 
            final MagicEvent event, 
            final Object data[], 
            final Object[] choiceResults) {
        throw new RuntimeException(getClass() + "did not override executeEvent");
    }
}
