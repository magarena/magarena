package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicPermanent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

import java.util.Set;

public class Naya_Hushblade {
    public static final MagicStatic S1 = Bant_Sureblade.S1;
    
    public static final MagicStatic S2 = new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (Bant_Sureblade.isValid(permanent)) {
                flags.add(MagicAbility.Shroud);
            }
        }
    }; 
}
