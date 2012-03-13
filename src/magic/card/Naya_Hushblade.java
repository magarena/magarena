package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class Naya_Hushblade {
	public static final MagicStatic S1 = Bant_Sureblade.S1;
	
	public static final MagicStatic S2 = new MagicStatic(MagicLayer.Ability) {
        @Override
        public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return (Bant_Sureblade.isValid(permanent,game)) ?
                flags | MagicAbility.Shroud.getMask() :
                flags;
        }
	}; 
}
