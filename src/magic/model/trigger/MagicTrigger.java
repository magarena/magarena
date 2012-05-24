package magic.model.trigger;

import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;

/** Lower priority values trigger before higher priority values. */
public abstract class MagicTrigger<T> implements MagicEventAction {

	private static final int DEFAULT_PRIORITY=10;
	
	private final int priority;
	private MagicCardDefinition cdef;

    protected MagicTrigger(final int priority) {
        this.priority = priority;
	}
	
	protected MagicTrigger() {
		this(DEFAULT_PRIORITY);
	}

    public void setCardDefinition(final MagicCardDefinition cdef) {
        this.cdef = cdef;
    }
	
	final MagicCardDefinition getCardDefinition() {
		return cdef;
	}
		
	public final int getPriority() {
		return priority;
	}
	
	public boolean usesStack() {
		return getType().usesStack();
	}

    @Override
    public void executeEvent(
            final MagicGame game, 
            final MagicEvent event, 
            final Object data[], 
            final Object[] choiceResults) {
        throw new RuntimeException(getClass() + "did not override executeEvent");
    }
	
    public abstract MagicTriggerType getType();
	
    public abstract MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final T data);
}
