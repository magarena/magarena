package magic.model.trigger;

import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;

/** Lower priority values trigger before higher priority values. */
public abstract class MagicTrigger implements MagicEventAction {

	private static final int DEFAULT_PRIORITY=10;
	
	private final MagicTriggerType type;
	private final int priority;
	private int cardIndex;

	private MagicTrigger(final MagicTriggerType type,final int cardIndex,final int priority) {
		this.type=type;
		this.cardIndex=cardIndex;
		this.priority=priority;
	}
    
    public MagicTrigger(final MagicTriggerType type, final int priority) {
		this(type,-1,priority);
	}
	
	public MagicTrigger(final MagicTriggerType type) {
		this(type,-1,DEFAULT_PRIORITY);
	}

    public void setCardIndex(final int cardIndex) {
        this.cardIndex = cardIndex;
    }
	
	public final MagicCardDefinition getCardDefinition() {
		return CardDefinitions.getInstance().getCard(cardIndex);
	}
	
	public final MagicTriggerType getType() {
		return type;
	}
		
	public final int getPriority() {
		return priority;
	}
	
	public boolean usesStack() {
		return type.usesStack();
	}
	
	public abstract MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data);
}
