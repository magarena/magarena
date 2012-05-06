package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class Grixis_Grimblade {
	public static final Object S1 = Bant_Sureblade.S1;
	
	public static final MagicStatic S2 = new MagicStatic(MagicLayer.Ability) {
        @Override
        public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return (Bant_Sureblade.isValid(permanent,game)) ?
                flags | MagicAbility.Deathtouch.getMask() :
                flags;
        }
	}; 
}
