package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

public class Raksha_Golden_Cub {
    public static final MagicStatic S = new MagicStatic(
        MagicLayer.ModPT, 
        MagicTargetFilter.TARGET_CAT_YOU_CONTROL) {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(2,2);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.isEquipped();
        }
    };
    
    public static final MagicStatic S2 = new MagicStatic(
            MagicLayer.Ability, 
            MagicTargetFilter.TARGET_CAT_YOU_CONTROL) {
            @Override
            public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
                return flags | MagicAbility.DoubleStrike.getMask();
            }
            @Override
            public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                return source.isEquipped();
            }
        };
}
