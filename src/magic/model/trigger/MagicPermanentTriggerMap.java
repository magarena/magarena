package magic.model.trigger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import magic.exception.GameException;
import magic.model.MagicCopyMap;
import magic.model.MagicPermanent;
import magic.model.MurmurHash3;

public class MagicPermanentTriggerMap {

    private final Map<MagicTriggerType,PriorityQueue<MagicPermanentTrigger>> effects =
        new EnumMap<MagicTriggerType,PriorityQueue<MagicPermanentTrigger>>(MagicTriggerType.class);

    // initializer block
    {
        for (final MagicTriggerType type : MagicTriggerType.values()) {
            effects.put(type, new PriorityQueue<MagicPermanentTrigger>());
        }
    }

    public MagicPermanentTriggerMap() {}

    public MagicPermanentTriggerMap(final MagicPermanentTriggerMap other) {
        for (final Map.Entry<MagicTriggerType, PriorityQueue<MagicPermanentTrigger>> type : other.effects.entrySet()) {
            for (final MagicPermanentTrigger mptrigger : type.getValue()) {
                add(mptrigger);
            }
        }
    }

    public MagicPermanentTriggerMap(final MagicCopyMap copyMap, final MagicPermanentTriggerMap other) {
        for (final Map.Entry<MagicTriggerType, PriorityQueue<MagicPermanentTrigger>> type : other.effects.entrySet()) {
            for (final MagicPermanentTrigger mptrigger : type.getValue()) {
                add(new MagicPermanentTrigger(copyMap, mptrigger));
            }
        }
    }

    public Collection<MagicPermanentTrigger> get(final MagicTriggerType type) {
        return effects.get(type);
    }

    public void add(final MagicPermanentTrigger mptrigger) {
        effects.get(mptrigger.getTrigger().getType()).add(mptrigger);
    }

    public Collection<MagicPermanentTrigger> remove(final MagicPermanent permanent) {
        final Collection<MagicPermanentTrigger> removedTriggers = new ArrayList<MagicPermanentTrigger>();
        for (final Map.Entry<MagicTriggerType, PriorityQueue<MagicPermanentTrigger>> type : effects.entrySet()) {
            final Collection<MagicPermanentTrigger> triggers = type.getValue();
            for (final Iterator<MagicPermanentTrigger> iterator = triggers.iterator();iterator.hasNext();) {
                final MagicPermanentTrigger permanentTrigger = iterator.next();
                if (permanentTrigger.getPermanent() == permanent) {
                    iterator.remove();
                    removedTriggers.add(permanentTrigger);
                }
            }
        }
        return removedTriggers;
    }

    public MagicPermanentTrigger remove(final MagicPermanent permanent, final MagicTrigger<?> trigger) {
        for (final Map.Entry<MagicTriggerType, PriorityQueue<MagicPermanentTrigger>> type : effects.entrySet()) {
            final Collection<MagicPermanentTrigger> triggers = type.getValue();
            for (final Iterator<MagicPermanentTrigger> iterator = triggers.iterator();iterator.hasNext();) {
                final MagicPermanentTrigger permanentTrigger = iterator.next();
                if (permanentTrigger.getPermanent() == permanent &&
                    permanentTrigger.getTrigger() == trigger) {
                    iterator.remove();
                    return permanentTrigger;
                }
            }
        }
        throw new GameException("Could not remove " + permanent + "'s trigger " + trigger, permanent.getGame());
    }

    public void remove(final MagicPermanentTrigger mptrigger) {
        effects.get(mptrigger.getTrigger().getType()).remove(mptrigger);
    }

    public long getStateId() {
        int size = 0;
        for (final MagicTriggerType type : MagicTriggerType.values()) {
            size += 2 * effects.get(type).size();
        }
        final long[] keys = new long[size];
        int idx = 0;
        for (final Map.Entry<MagicTriggerType, PriorityQueue<MagicPermanentTrigger>> type : effects.entrySet()) {
            for (final MagicPermanentTrigger mptrigger : type.getValue()) {
                keys[idx] = mptrigger.getPermanent().getStateId(); idx++;
                keys[idx] = mptrigger.getTrigger().hashCode(); idx++;
            }
        }
        return MurmurHash3.hash(keys);
    }
}
