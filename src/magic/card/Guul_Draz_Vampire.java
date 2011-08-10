package magic.card;
import java.util.*;
import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.trigger.*;
import magic.model.condition.*;
import magic.model.*;
import magic.data.*;
import magic.model.variable.*;

public class Guul_Draz_Vampire {
	
	private static final MagicLocalVariable GUUL_DRAZ_VAMPIRE=new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			if (game.getOpponent(permanent.getController()).getLife()<=10) {
				pt.power+=2;
				pt.toughness++;
			}
		}	
		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return game.getOpponent(permanent.getController()).getLife()<=10?flags|MagicAbility.Intimidate.getMask():flags;
		}
	};
    
    public static final MagicChangeCardDefinition SET = new MagicChangeCardDefinition() {
        @Override
        public void change(MagicCardDefinition cdef) {
		    cdef.addLocalVariable(MagicStaticLocalVariable.getInstance());
    		cdef.addLocalVariable(GUUL_DRAZ_VAMPIRE);
        }
    };
    
}
