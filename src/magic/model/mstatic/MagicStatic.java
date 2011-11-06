package magic.model.mstatic;

import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPowerToughness;
import magic.model.MagicSubType;
import magic.model.MagicPermanent;
import magic.model.target.MagicTargetFilter;
import java.util.EnumSet;

public abstract class MagicStatic extends MagicDummyPermanentModifier {

    public static final boolean UntilEOT = true;

    //permanents affected by the static effect
    protected MagicTargetFilter filter;

    //layer where this effect operate
	private final MagicLayer layer;

    //card definition providing the effect
	private int cardIndex;

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

    public void setCardIndex(final int cardIndex) {
        this.cardIndex = cardIndex;
    }
	
	final MagicCardDefinition getCardDefinition() {
		return CardDefinitions.getInstance().getCard(cardIndex);
	}
		
	public final MagicLayer getLayer() {
		return layer;
	}

    public final boolean isUntilEOT() {
        return isUntilEOT;
    }

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
                return (source.isEquipment()) ? 
                    source.getEquippedCreature() == target :
                    source.getEnchantedCreature() == target;
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
                return (source.isEquipment()) ? 
                    source.getEquippedCreature() == target :
                    source.getEnchantedCreature() == target;
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
                return (source.isEquipment()) ? 
                    source.getEquippedCreature() == target :
                    source.getEnchantedCreature() == target;
            }
        };
	}
}
