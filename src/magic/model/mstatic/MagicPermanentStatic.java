package magic.model.mstatic;

import magic.model.MagicCopyMap;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.MagicCounterType;
import magic.model.target.MagicTargetFilter;

public class MagicPermanentStatic implements Comparable<MagicPermanentStatic> {
    public static final MagicPermanentStatic CountersEffect = 
        new MagicPermanentStatic(0, MagicPermanent.NONE, new MagicStatic(
            MagicLayer.CountersPT, 
            MagicTargetFilter.TARGET_CREATURE) {
            @Override
            public void modPowerToughness(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
                final int amt = permanent.getCounters(MagicCounterType.PlusOne) - 
                                permanent.getCounters(MagicCounterType.MinusOne);
                pt.add(amt,amt);
            }
        });

    private final long id;
    private final MagicPermanent permanent;
    private final MagicStatic mstatic;
    
    public MagicPermanentStatic(final long id,final MagicPermanent permanent,final MagicStatic mstatic) {
        this.id=id;
        this.permanent=permanent;
        this.mstatic=mstatic;
    }
    
    public MagicPermanentStatic(final MagicCopyMap copyMap,final MagicPermanentStatic source) {
        this(source.id, copyMap.copy(source.permanent), source.mstatic);
    }
    
    public long getId() {
        return id;
    }
    
    public MagicPermanent getPermanent() {
        return permanent;
    }
    
    public MagicStatic getStatic() {
        return mstatic;
    }
    
    public MagicLayer getLayer() {
        return mstatic.getLayer();
    }
    
    @Override
    public int compareTo(final MagicPermanentStatic permanentStatic) {
        //sort by layer
        final int layerDif = mstatic.getLayer().compareTo(permanentStatic.mstatic.getLayer());
        if (layerDif != 0) {
            return layerDif;
        } 
        
        //break ties by id
        return Long.signum(id-permanentStatic.id);
    }
}
