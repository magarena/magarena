package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicObject;

public class MagicCounterChangeTriggerData {

    public final MagicPlayer player;
    public final MagicObject obj;
    public final MagicCounterType counterType;
    public final int amount;

    public MagicCounterChangeTriggerData(final MagicPlayer aPlayer, final MagicObject aObj, final MagicCounterType aCounterType, final int aAmount) {
        this.player = aPlayer;
        this.obj = aObj;
        this.counterType = aCounterType;
        this.amount = aAmount;
    }
}
