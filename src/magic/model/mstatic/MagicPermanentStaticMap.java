package magic.model.mstatic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import magic.model.MagicCopyMap;
import magic.model.MagicPermanent;
import magic.model.MurmurHash3;

public class MagicPermanentStaticMap {

    private final Map<MagicLayer,SortedSet<MagicPermanentStatic>> effects =
            new EnumMap<>(MagicLayer.class);

    public MagicPermanentStaticMap() {
        for (final MagicLayer layer : MagicLayer.values()) {
            effects.put(layer, new TreeSet<>());
        }
        //changes to power and toughness due to +1/+1 and -1/-1 counters
        add(MagicPermanentStatic.CountersEffect);

        //Handles Basic Land mana abilities.
        add(MagicPermanentStatic.BasicLandEffect);
    }

    public MagicPermanentStaticMap(final MagicCopyMap copyMap, final MagicPermanentStaticMap sourceMap) {
        for (final MagicLayer layer : MagicLayer.values()) {
            effects.put(layer, new TreeSet<>());
        }

        for (final Map.Entry<MagicLayer, SortedSet<MagicPermanentStatic>> layer : sourceMap.effects.entrySet()) {
            for (final MagicPermanentStatic mpstatic : layer.getValue()) {
                add(new MagicPermanentStatic(copyMap, mpstatic));
            }
        }
    }

    public SortedSet<MagicPermanentStatic> get(final MagicLayer layer) {
        return effects.get(layer);
    }

    public void add(final MagicPermanentStatic mpstatic) {
        effects.get(mpstatic.getLayer()).add(mpstatic);
    }

    public Collection<MagicPermanentStatic> remove(final MagicPermanent permanent) {
        final Collection<MagicPermanentStatic> removedStatics = new ArrayList<>();
        for (final Map.Entry<MagicLayer, SortedSet<MagicPermanentStatic>> layer : effects.entrySet()) {
            final Collection<MagicPermanentStatic> statics = layer.getValue();
            for (final Iterator<MagicPermanentStatic> iterator = statics.iterator();iterator.hasNext();) {
                final MagicPermanentStatic permanentStatic = iterator.next();
                if (permanentStatic.getPermanent() == permanent) {
                    iterator.remove();
                    removedStatics.add(permanentStatic);
                }
            }
        }
        return removedStatics;
    }

    public Collection<MagicPermanentStatic> removeTurn() {
        final Collection<MagicPermanentStatic> removedStatics = new ArrayList<>();
        for (final Map.Entry<MagicLayer, SortedSet<MagicPermanentStatic>> layer : effects.entrySet()) {
            final Collection<MagicPermanentStatic> statics = layer.getValue();
            for (final Iterator<MagicPermanentStatic> iterator = statics.iterator();iterator.hasNext();) {
                final MagicPermanentStatic permanentStatic = iterator.next();
                if (permanentStatic.getStatic().isUntilEOT()) {
                    iterator.remove();
                    removedStatics.add(permanentStatic);
                }
            }
        }
        return removedStatics;
    }

    public void remove(final MagicPermanent permanent, final MagicStatic mstatic) {
        final Collection<MagicPermanentStatic> statics = effects.get(mstatic.getLayer());
        for (final Iterator<MagicPermanentStatic> iterator = statics.iterator();iterator.hasNext();) {
            final MagicPermanentStatic permanentStatic = iterator.next();
            if (permanentStatic.getPermanent() == permanent && permanentStatic.getStatic() == mstatic) {
                iterator.remove();
                return;
            }
        }
        throw new RuntimeException("nothing to remove");
    }

    public Collection<MagicPermanentStatic> remove(final MagicPermanent permanent, final Collection<MagicStatic> statics) {
        final Collection<MagicPermanentStatic> removedStatics = new ArrayList<>();
        for (final MagicStatic mstatic : statics) {
            final Collection<MagicPermanentStatic> mpstatics = effects.get(mstatic.getLayer());
            for (final Iterator<MagicPermanentStatic> iterator = mpstatics.iterator();iterator.hasNext();) {
                final MagicPermanentStatic permanentStatic = iterator.next();
                if (permanentStatic.getPermanent() == permanent && permanentStatic.getStatic() == mstatic) {
                    iterator.remove();
                    removedStatics.add(permanentStatic);
                    break;
                }
            }
        }
        return removedStatics;
    }

    public long getStateId() {
        int size = 0;
        for (final MagicLayer layer : MagicLayer.values()) {
            size += 2 * effects.get(layer).size();
        }
        final long[] keys = new long[size];
        int idx = 0;
        for (final Map.Entry<MagicLayer, SortedSet<MagicPermanentStatic>> layer : effects.entrySet()) {
            for (final MagicPermanentStatic mpstatic : layer.getValue()) {
                keys[idx] = mpstatic.getPermanent().getStateId(); idx++;
                keys[idx] = mpstatic.getStatic().getStateId(); idx++;
            }
        }
        return MurmurHash3.hash(keys);
    }
}
