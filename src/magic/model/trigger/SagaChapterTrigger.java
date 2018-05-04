package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.target.MagicTargetFilter;

public abstract class SagaChapterTrigger extends OneOrMoreCountersArePutTrigger {

    protected int chapter;

    public SagaChapterTrigger(final int priority, final int chapter) {
        super(priority);
        this.chapter = chapter;
    }

    public SagaChapterTrigger(final int chapter) {
        this.chapter = chapter;
    }

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
        final int current = permanent.getCounters(MagicCounterType.Lore);
        final int before = current - data.amount;
        return super.accept(permanent, data) &&
            permanent == data.obj &&
            data.counterType == MagicCounterType.Lore &&
            before < chapter &&
            current >= chapter;
    }

    public static SagaChapterTrigger create(final int chapter, final MagicSourceEvent sourceEvent) {
        return new SagaChapterTrigger(chapter) {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
                return sourceEvent.getTriggerEvent(permanent);
            }
        };
    }

}
