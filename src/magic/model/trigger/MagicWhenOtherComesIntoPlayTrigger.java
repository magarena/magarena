package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;

public abstract class MagicWhenOtherComesIntoPlayTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenOtherComesIntoPlayTrigger(final int priority) {
        super(MagicTriggerType.WhenOtherComesIntoPlay, priority); 
	}
	
	public MagicWhenOtherComesIntoPlayTrigger() {
		super(MagicTriggerType.WhenOtherComesIntoPlay);
	}
}
