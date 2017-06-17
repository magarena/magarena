package magic.model.trigger;

import magic.model.MagicPlayer;

public abstract class ClashTrigger extends MagicTrigger<MagicPlayer> {
    public ClashTrigger(final int priority) {
        super(priority);
    }

    public ClashTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenClash;
    }
}
