package magic.model.trigger;

import magic.model.MagicObject;

public class MagicCounterChangeTriggerData {

    public final MagicObject object;
    public final int amount;

    public MagicCounterChangeTriggerData(final MagicObject aObject, final int aAmount) {
        this.object = aObject;
        this.amount = aAmount;
    }
}
