
package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;

public abstract class MagicWhenLifeIsGainedTrigger extends MagicTrigger<MagicPlayer> {
    public MagicWhenLifeIsGainedTrigger(final int priority) {
        super(MagicTriggerType.WhenLifeIsGained, priority); 
	}
	
	public MagicWhenLifeIsGainedTrigger() {
		super(MagicTriggerType.WhenLifeIsGained);
	}
}
