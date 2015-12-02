package magic.model.trigger;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("serial")
public class MagicPermanentTriggerList extends ArrayList<MagicPermanentTrigger> {

    public MagicPermanentTriggerList() {}

    public MagicPermanentTriggerList(final MagicPermanentTriggerList triggerList) {
        super(triggerList);
    }

    public MagicPermanentTriggerList(final MagicPermanentTriggerMap triggerMap,final MagicPermanentTriggerList triggerList) {
        for (final MagicPermanentTrigger permanentTrigger : triggerList) {
            final long id = permanentTrigger.getId();
            final Collection<MagicPermanentTrigger> triggersSet = triggerMap.get(permanentTrigger.getTrigger().getType());
            for (final MagicPermanentTrigger otherPermanentTrigger : triggersSet) {
                if (otherPermanentTrigger.getId() == id) {
                    add(otherPermanentTrigger);
                    break;
                }
            }
        }
    }
}
