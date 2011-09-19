package magic.model.mstatic; 

import magic.model.MagicGame;
import magic.model.MagicSubType;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;

import java.util.EnumSet;

public enum MagicLayer {
    Copy,        //1.  copy 
    Control,     //2.  control changing
    Text,        //3.  text changing
    CDASubtype,  //4a. CDA subtype
    Type,        //4b. type-changing (include sub and super types)
    CDAColor,    //5a. CDA color
    Color,       //5b. color changing
    Ability,     //6.  ability adding/removing
    CDAPT,       //7a. CDA p/t
    SetPT,       //7b. set p/t to specific value
    ModPT,       //7c. modify p/t
    CountersPT,  //7d. p/t changes due to counters
    SwitchPT,    //7e. switch p/t 
    Player,      //8.  affect player, not objects
    Game,        //9.  affect game rules, not objects
    ;
        
    public static void getPowerToughness(final MagicGame game, final MagicPermanent permanent, final MagicPowerToughness pt) {
        for (final MagicLayer layer : EnumSet.range(MagicLayer.SetPT, MagicLayer.SwitchPT)) {
            for (final MagicPermanentStatic mpstatic : game.getStatics(layer)) {
                final MagicStatic mstatic = mpstatic.getStatic();
                if (mstatic.accept(game, mpstatic.getPermanent(),permanent)) {
                    mstatic.getPowerToughness(game, permanent, pt);
                }
            }
        }
    }
    
    public static long getAbilityFlags(final MagicGame game, final MagicPermanent permanent, long flags) {
        for (final MagicPermanentStatic mpstatic : game.getStatics(MagicLayer.Ability)) {
            final MagicStatic mstatic = mpstatic.getStatic();
            if (mstatic.accept(game, mpstatic.getPermanent(),permanent)) {
                flags = mstatic.getAbilityFlags(game, permanent, flags);
            }
        }
        return flags;
    }
        
    public static int getTypeFlags(final MagicGame game, final MagicPermanent permanent, int flags) {
        for (final MagicPermanentStatic mpstatic : game.getStatics(MagicLayer.Type)) {
            final MagicStatic mstatic = mpstatic.getStatic();
            if (mstatic.accept(game, mpstatic.getPermanent(),permanent)) {
			    flags = mstatic.getTypeFlags(permanent,flags);
            }
		}
        return flags;
    }
    
    public static EnumSet<MagicSubType> getSubTypeFlags(
            final MagicGame game, 
            final MagicPermanent permanent, 
            EnumSet<MagicSubType> flags) {
        for (final MagicPermanentStatic mpstatic : game.getStatics(MagicLayer.Type)) {
            final MagicStatic mstatic = mpstatic.getStatic();
            if (mstatic.accept(game, mpstatic.getPermanent(),permanent)) {
			    flags = mstatic.getSubTypeFlags(permanent,flags);
            }
		}
        return flags;
    }
    
    public static int getColorFlags(final MagicGame game, final MagicPermanent permanent, int flags) {
        for (final MagicPermanentStatic mpstatic : game.getStatics(MagicLayer.Type)) {
            final MagicStatic mstatic = mpstatic.getStatic();
            if (mstatic.accept(game, mpstatic.getPermanent(),permanent)) {
			    flags = mstatic.getColorFlags(permanent,flags);
            }
		}
        return flags;
    }
}
