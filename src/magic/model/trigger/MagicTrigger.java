package magic.model.trigger;

import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicDamage;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicItemOnStack;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.trigger.MagicGraveyardTriggerData;

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
	
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent) {
        throw new RuntimeException("Did not override executeTrigger () method");
    }
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage data) {
        throw new RuntimeException("Did not override executeTrigger (MagicDamage) method");
    }
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
        throw new RuntimeException("Did not override executeTrigger (MagicPlayer) method");
    }
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack data) {
        throw new RuntimeException("Did not override executeTrigger (MagicCardOnStack) method");
    }
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
        throw new RuntimeException("Did not override executeTrigger (MagicPermanent) method");
    }
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicGraveyardTriggerData data) {
        throw new RuntimeException("Did not override executeTrigger (MagicPermanent) method");
    }
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicItemOnStack data) {
        throw new RuntimeException("Did not override executeTrigger (MagicCardOnStack) method");
    }

	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
        if (data == null) {
            return executeTrigger(game, permanent);
        } else if (data instanceof MagicDamage) {
            return executeTrigger(game, permanent, (MagicDamage)data);
        } else if (data instanceof MagicPlayer) {
            return executeTrigger(game, permanent, (MagicPlayer)data);
        } else if (data instanceof MagicCardOnStack) {
            return executeTrigger(game, permanent, (MagicCardOnStack)data);
        } else if (data instanceof MagicPermanent) {
            return executeTrigger(game, permanent, (MagicPermanent)data);
        } else if (data instanceof MagicGraveyardTriggerData) {
            return executeTrigger(game, permanent, (MagicGraveyardTriggerData)data);
        } else if (data instanceof MagicItemOnStack) {
            return executeTrigger(game, permanent, (MagicItemOnStack)data);
        }
        throw new RuntimeException("Did not override executeTrigger (Object) method");
    }
}
