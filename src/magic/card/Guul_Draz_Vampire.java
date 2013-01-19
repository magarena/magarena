package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

import java.util.Set;

public class Guul_Draz_Vampire {
    public static final MagicStatic S1 = new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            if (permanent.getOpponent().getLife() <= 10) {
                pt.add(2,1);
            }
        }        
    };
    public static final MagicStatic S2 = new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (permanent.getOpponent().getLife() <= 10) { 
                flags.add(MagicAbility.Intimidate);
            }
        }
    };
}
