package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;

public abstract class MagicAtEndOfTurnTrigger extends MagicTrigger<MagicPlayer> {
    public MagicAtEndOfTurnTrigger(final int priority) {
        super(MagicTriggerType.AtEndOfTurn, priority); 
	}
	
	public MagicAtEndOfTurnTrigger() {
		super(MagicTriggerType.AtEndOfTurn);
	}
}
