package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicDamage;
import magic.model.event.MagicEvent;

public abstract class MagicWhenPutIntoGraveyardTrigger extends MagicTrigger<MagicGraveyardTriggerData> {
    public MagicWhenPutIntoGraveyardTrigger(final int priority) {
        super(MagicTriggerType.WhenPutIntoGraveyard, priority); 
	}
	
	public MagicWhenPutIntoGraveyardTrigger() {
		super(MagicTriggerType.WhenPutIntoGraveyard);
	}
}
