package magic.model.mstatic;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Collection;

import magic.model.MagicCopyMap;
import magic.model.MagicPermanent;

public class MagicPermanentStaticMap {
    
    private final Map<MagicLayer,List<MagicPermanentStatic>> effects = 
        new EnumMap<MagicLayer,List<MagicPermanentStatic>>(MagicLayer.class);
	
	public MagicPermanentStaticMap() {
        for (MagicLayer layer : MagicLayer.values()) {
            effects.put(layer, new LinkedList<MagicPermanentStatic>());
        }
        //changes to power and toughness due to +1/+1 and -1/-1 counters
        add(MagicPermanentStatic.CountersEffect);
    }
	
    public MagicPermanentStaticMap(final MagicCopyMap copyMap, final MagicPermanentStaticMap sourceMap) {
        for (MagicLayer layer : MagicLayer.values()) {
            effects.put(layer, new LinkedList<MagicPermanentStatic>());
        }
	    
        for (final Map.Entry<MagicLayer, List<MagicPermanentStatic>> layer : sourceMap.effects.entrySet()) {
            for (final MagicPermanentStatic mpstatic : layer.getValue()) {
                add(new MagicPermanentStatic(copyMap, mpstatic));
            }
        }
    }

    public List<MagicPermanentStatic> get(final MagicLayer layer) {
        return effects.get(layer);
    }

    public void add(final MagicPermanentStatic mpstatic) {
        effects.get(mpstatic.getLayer()).add(mpstatic);
    }
    
    public void remove(final MagicPermanent permanent,final Collection<MagicPermanentStatic> removedStatics) {
	    for (final Map.Entry<MagicLayer, List<MagicPermanentStatic>> layer : effects.entrySet()) {
            final List<MagicPermanentStatic> statics = layer.getValue();
            for (final Iterator<MagicPermanentStatic> iterator = statics.iterator();iterator.hasNext();) {
                final MagicPermanentStatic permanentStatic = iterator.next();
                if (permanentStatic.getPermanent() == permanent) {
                    iterator.remove();
                    removedStatics.add(permanentStatic);
                }
            }
        }
    }
        
    public void remove(final MagicPermanent permanent, final MagicStatic mstatic) {
        final List<MagicPermanentStatic> statics = effects.get(mstatic.getLayer());
        for (final Iterator<MagicPermanentStatic> iterator = statics.iterator();iterator.hasNext();) {
            final MagicPermanentStatic permanentStatic = iterator.next();
            if (permanentStatic.getPermanent() == permanent && permanentStatic.getStatic() == mstatic) {
                iterator.remove();
            }
        }
    }
}
