package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;

public abstract class MagicWhenComesIntoPlayTrigger extends MagicTrigger<MagicPlayer> {
    public MagicWhenComesIntoPlayTrigger(final int priority) {
        super(MagicTriggerType.WhenComesIntoPlay, priority); 
	}
	
	public MagicWhenComesIntoPlayTrigger() {
		super(MagicTriggerType.WhenComesIntoPlay);
	}
}
