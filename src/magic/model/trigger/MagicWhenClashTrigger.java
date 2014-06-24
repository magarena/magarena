package magic.model.trigger;

import magic.model.MagicPlayer;

public abstract class MagicWhenClashTrigger extends MagicTrigger<MagicPlayer> {
    public MagicWhenClashTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenClashTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenClash;
    }
}
