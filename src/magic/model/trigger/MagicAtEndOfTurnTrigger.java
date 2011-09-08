package magic.model.trigger;

import magic.model.MagicPlayer;

public abstract class MagicAtEndOfTurnTrigger extends MagicTrigger<MagicPlayer> {
    public MagicAtEndOfTurnTrigger(final int priority) {
        super(priority); 
	}
	
	public MagicAtEndOfTurnTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.AtEndOfTurn;
    }
}
