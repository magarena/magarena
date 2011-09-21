package magic.model.trigger;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map;
import java.util.EnumMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;

import magic.model.MagicCopyMap;
import magic.model.MagicPermanent;

public class MagicPermanentTriggerMap {
    
    private final Map<MagicTriggerType,SortedSet<MagicPermanentTrigger>> effects = 
        new EnumMap<MagicTriggerType,SortedSet<MagicPermanentTrigger>>(MagicTriggerType.class);
	
	public MagicPermanentTriggerMap() {
        for (MagicTriggerType type : MagicTriggerType.values()) {
            effects.put(type, new TreeSet<MagicPermanentTrigger>());
        }
    }
	
    public MagicPermanentTriggerMap(final MagicCopyMap copyMap, final MagicPermanentTriggerMap sourceMap) {
        for (MagicTriggerType type : MagicTriggerType.values()) {
            effects.put(type, new TreeSet<MagicPermanentTrigger>());
        }
	    
        for (final Map.Entry<MagicTriggerType, SortedSet<MagicPermanentTrigger>> type : sourceMap.effects.entrySet()) {
            for (final MagicPermanentTrigger mptrigger : type.getValue()) {
                add(new MagicPermanentTrigger(copyMap, mptrigger));
            }
        }
    }

    public SortedSet<MagicPermanentTrigger> get(final MagicTriggerType type) {
        return effects.get(type);
    }

    public void add(final MagicPermanentTrigger mptrigger) {
        effects.get(mptrigger.getTrigger().getType()).add(mptrigger);
    }
    
    public Collection<MagicPermanentTrigger> remove(final MagicPermanent permanent) {
        final Collection<MagicPermanentTrigger> removedTriggers = new ArrayList<MagicPermanentTrigger>();
	    for (final Map.Entry<MagicTriggerType, SortedSet<MagicPermanentTrigger>> type : effects.entrySet()) {
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
        
    public void remove(final MagicPermanentTrigger mptrigger) {
        effects.get(mptrigger.getTrigger().getType()).remove(mptrigger);
    }
}
