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
	private MagicCardDefinition card;

	private MagicTrigger(final MagicTriggerType type,final MagicCardDefinition card,final int priority) {
		
		this.type=type;
		this.card=card;
		this.priority=priority;
	}
	
	public MagicTrigger(final MagicTriggerType type,final String name,final int priority) {
		
		this(type,CardDefinitions.getInstance().getCard(name),priority);
	}
	
	public MagicTrigger(final MagicTriggerType type,final String name) {
		
		this(type,name,DEFAULT_PRIORITY);
	}
	
	public MagicTrigger(final MagicTriggerType type) {
		
		this(type,(MagicCardDefinition)null,DEFAULT_PRIORITY);
	}

    public void setCardDefinition(final MagicCardDefinition card) {
        this.card = card;
    }
	
	public final MagicCardDefinition getCardDefinition() {
		
		return card;
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
