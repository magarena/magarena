package magic.model.trigger;

import magic.model.MagicPlayer;

public class MagicLifeChangeTriggerData {

    public final MagicPlayer player;
    public final int amount;

    public MagicLifeChangeTriggerData(final MagicPlayer aPlayer, final int aAmount) {
        this.player = aPlayer;
        this.amount = aAmount;
    }
}
