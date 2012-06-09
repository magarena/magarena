package magic.model.mstatic;

import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPowerToughness;
import magic.model.MagicSubType;
import magic.model.MagicPermanent;
import magic.model.target.MagicTargetFilter;
import java.util.EnumSet;

public abstract class MagicStatic extends MagicDummyPermanentModifier implements MagicChangeCardDefinition {

    public static final boolean UntilEOT = true;

    //permanents affected by the static effect
    protected MagicTargetFilter filter;

    //layer where this effect operate
	private final MagicLayer layer;

    //card definition providing the effect
	private MagicCardDefinition cdef;

    private boolean isUntilEOT;

    protected MagicStatic(final MagicLayer aLayer, final MagicTargetFilter aFilter, final boolean aIsUntilEOT) {
        filter = aFilter;
        layer = aLayer;
        isUntilEOT = aIsUntilEOT;
	}
    
    protected MagicStatic(final MagicLayer aLayer, final MagicTargetFilter aFilter) {
        this(aLayer, aFilter, false);
	}
    
    protected MagicStatic(final MagicLayer aLayer, final boolean aIsUntilEOT) {
        this(aLayer, MagicTargetFilter.SELF, aIsUntilEOT);
	}
    
    protected MagicStatic(final MagicLayer aLayer) {
        this(aLayer, MagicTargetFilter.SELF, false);
	}

    public void setCardDefinition(final MagicCardDefinition cdef) {
        this.cdef = cdef;
    }
	
	final MagicCardDefinition getCardDefinition() {
		return cdef;
	}
    
    @Override
    public void change(MagicCardDefinition cdef) {
        cdef.addStatic(this);
        setCardDefinition(cdef);
    }
		
	public final MagicLayer getLayer() {
		return layer;
	}

    public final boolean isUntilEOT() {
        return isUntilEOT;
    }

    public void setSource(final MagicPermanent source) {}

    public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
        if (filter == MagicTargetFilter.SELF) {
            return source == target;
        } else {
            return filter.accept(game, source.getController(), target) && condition(game, source, target);
        }
    }
    
    public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
        return true;
    }

    private static boolean acceptLinked(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
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
            public void getPowerToughness(
                final MagicGame game,
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
                final MagicGame game,
                final MagicPermanent permanent,
                long flags) {
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
            public EnumSet<MagicSubType> getSubTypeFlags(
                final MagicPermanent permanent,
                final EnumSet<MagicSubType> flags) {
                final EnumSet<MagicSubType> mod = flags.clone();
                mod.addAll(givenSubTypeFlags);
                return mod;
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
