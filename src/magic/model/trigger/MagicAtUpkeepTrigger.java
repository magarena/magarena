package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;

public abstract class MagicAtUpkeepTrigger extends MagicTrigger<MagicPlayer> {
    public MagicAtUpkeepTrigger(final int priority) {
        super(MagicTriggerType.AtUpkeep, priority); 
	}
	
	public MagicAtUpkeepTrigger() {
		super(MagicTriggerType.AtUpkeep);
	}
}
