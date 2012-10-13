package magic.model.mstatic;

import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.MagicSubType;
import magic.model.target.MagicTargetFilter;

import java.util.EnumSet;

public abstract class MagicStatic extends MagicDummyPermanentModifier implements MagicChangeCardDefinition {

    public static final boolean UntilEOT = true;
    public static final boolean Forever = !UntilEOT;

    //permanents affected by the static effect
    private MagicTargetFilter<MagicPermanent> filter;

    //layer where this effect operate
    private final MagicLayer layer;

    private final boolean isUntilEOT;

    protected MagicStatic(final MagicLayer aLayer, final MagicTargetFilter<MagicPermanent> aFilter, final boolean aIsUntilEOT) {
        filter = aFilter;
        layer = aLayer;
        isUntilEOT = aIsUntilEOT;
    }
    
    protected MagicStatic(final MagicLayer aLayer, final MagicTargetFilter<MagicPermanent> aFilter) {
        this(aLayer, aFilter, false);
    }
    
    protected MagicStatic(final MagicLayer aLayer, final boolean aIsUntilEOT) {
        this(aLayer, MagicTargetFilter.NONE, aIsUntilEOT);
    }
    
    protected MagicStatic(final MagicLayer aLayer) {
        this(aLayer, MagicTargetFilter.NONE, false);
    }
    
    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addStatic(this);
    }
        
    public final MagicLayer getLayer() {
        return layer;
    }

    public final boolean isUntilEOT() {
        return isUntilEOT;
    }

    public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
        if (filter == MagicTargetFilter.NONE) {
            return source == target && condition(game, source, target);
        } else {
            return filter.accept(game, source.getController(), target) && condition(game, source, target);
        }
    }
    
    public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
        return true;
    }

    public static boolean acceptLinked(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
        if (source.isEquipment()) {
            return source.getEquippedCreature() == target;
        } else if (source.isEnchantment()) {
            return source.getEnchantedCreature() == target;
        } else {
            return source.getPairedCreature() == target ||
                (source == target && source.isPaired());
        }
    }
    
    public static MagicStatic genPTStatic(final int givenPower, final int givenToughness) {
        return new MagicStatic(MagicLayer.ModPT) {
            @Override
            public void modPowerToughness(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
                pt.add(givenPower, givenToughness);
            }
            @Override
            public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                return MagicStatic.acceptLinked(game, source, target);
            }
        };
    }

    public static MagicStatic genABStatic(final long givenAbilityFlags) {
        return new MagicStatic(MagicLayer.Ability) {
            @Override
            public long getAbilityFlags(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final long flags) {
                return flags | givenAbilityFlags;
            }
            @Override
            public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                return MagicStatic.acceptLinked(game, source, target);
            }
        };
    }
        
    public static MagicStatic genSTStatic(final EnumSet<MagicSubType> givenSubTypeFlags) {
        return new MagicStatic(MagicLayer.Type) {
            @Override
            public void modSubTypeFlags(
                final MagicPermanent permanent,
                final EnumSet<MagicSubType> flags) {
                flags.addAll(givenSubTypeFlags);
            }
            @Override
            public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                return MagicStatic.acceptLinked(game, source, target);
            }
        };
    }
    
    public static MagicStatic genCOStatic(final int givenColorFlags) {
        return new MagicStatic(MagicLayer.Color) {
            @Override
            public int getColorFlags(
                final MagicPermanent permanent,
                final int flags) {
                return flags | givenColorFlags;
            }
            @Override
            public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                return MagicStatic.acceptLinked(game, source, target);
            }
        };
    }
}
