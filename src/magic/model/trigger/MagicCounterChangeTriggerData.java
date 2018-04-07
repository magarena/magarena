package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicObject;

public class MagicCounterChangeTriggerData {

    public final MagicObject obj;
    public final MagicCounterType counterType;
    public final int amount;

    public MagicCounterChangeTriggerData(final MagicObject aObj, final MagicCounterType aCounterType, final int aAmount) {
        this.obj = aObj;
        this.counterType = aCounterType;
        this.amount = aAmount;
    }
}
