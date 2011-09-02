package magic.model.trigger;

import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicCard;
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

    public MagicTrigger(final MagicTriggerType type, final int priority) {
		this.type = type; 
        this.priority = priority;
	}
	
	public MagicTrigger(final MagicTriggerType type) {
		this(type,DEFAULT_PRIORITY);
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
        throw new RuntimeException(getClass() + " did not override executeTrigger () method");
    }
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage data) {
        throw new RuntimeException(getClass() + " did not override executeTrigger (MagicDamage) method");
    }
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
        throw new RuntimeException(getClass() + " did not override executeTrigger (MagicPlayer) method");
    }
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack data) {
        throw new RuntimeException(getClass() + " did not override executeTrigger (MagicCardOnStack) method");
    }
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
        throw new RuntimeException(getClass() + " did not override executeTrigger (MagicPermanent) method");
    }
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicGraveyardTriggerData data) {
        throw new RuntimeException(getClass() + " did not override executeTrigger (MagicGraveyardTriggerData) method");
    }
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicItemOnStack data) {
        throw new RuntimeException(getClass() + " did not override executeTrigger (MagicItemOnStack) method");
    }
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCard data) {
        throw new RuntimeException(getClass() + " did not override executeTrigger (MagicCard) method");
    }

	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
        switch (type) {
	        case WhenComesIntoPlay:
                return executeTrigger(game, permanent);
	        case WhenPutIntoGraveyard:
                return executeTrigger(game, permanent, (MagicGraveyardTriggerData)data);
            case WhenTargeted:
                return executeTrigger(game, permanent, (MagicItemOnStack)data);
	        case WhenSpellIsPlayed:
                return executeTrigger(game, permanent, (MagicCardOnStack)data);
	        case WhenDiscarded:
            case WhenDrawn:
                return executeTrigger(game, permanent, (MagicCard)data);
            case WhenDamageIsDealt:
	        case IfDamageWouldBeDealt:
                return executeTrigger(game, permanent, (MagicDamage)data);
	        case AtUpkeep:
	        case AtEndOfTurn:
	        case WhenLifeIsGained:
                return executeTrigger(game, permanent, (MagicPlayer)data);
            case WhenBecomesTapped:
            case WhenOtherComesIntoPlay:
            case WhenOtherPutIntoGraveyardFromPlay:
            case WhenAttacks:
            case WhenBlocks:
                return executeTrigger(game, permanent, (MagicPermanent)data);
        }
        throw new RuntimeException(getClass() + " did not override executeTrigger (Object) method");
    }
}
