package magic.model.trigger;

import magic.model.MagicCopyMap;
import magic.model.MagicPermanent;

public class MagicPermanentTrigger implements Comparable<MagicPermanentTrigger> {

    private final long id;
    private final MagicPermanent permanent;
    private final MagicTrigger<?> trigger;

    public MagicPermanentTrigger(final long aId,final MagicPermanent aPermanent,final MagicTrigger<?> aTrigger) {
        id = aId;
        permanent = aPermanent;
        trigger = aTrigger;
    }

    public MagicPermanentTrigger(final MagicCopyMap copyMap,final MagicPermanentTrigger source) {
        this(source.id, copyMap.copy(source.permanent), source.trigger);
    }

    public long getId() {
        return id;
    }

    public MagicPermanent getPermanent() {
        return permanent;
    }

    public MagicTrigger<?> getTrigger() {
        return trigger;
    }

    @Override
    public int compareTo(final MagicPermanentTrigger permanentTrigger) {
        //sort by priority, break ties by id
        final int priorityDiff = trigger.getPriority() - permanentTrigger.trigger.getPriority();
        return (priorityDiff != 0) ?
            priorityDiff :
            Long.signum(id-permanentTrigger.id);
    }
}
