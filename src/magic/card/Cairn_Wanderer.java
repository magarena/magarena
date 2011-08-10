package magic.card;

import java.util.Arrays;

import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.trigger.*;
import magic.model.condition.*;
import magic.model.variable.*;
import magic.model.*;

public class Cairn_Wanderer {
	private static final MagicLocalVariable CAIRN_WANDERER=new MagicDummyLocalVariable() {
		@Override
		public long getAbilityFlags(MagicGame game,MagicPermanent permanent,long flags) {
			long newFlags=0;
			for (final MagicPlayer player : game.getPlayers()) {
				for (final MagicCard card : player.getGraveyard()) {
					final MagicCardDefinition cardDefinition=card.getCardDefinition();
					if (cardDefinition.isCreature()) {
						newFlags|=cardDefinition.getAbilityFlags();
					}
				}
			}
			return flags|(newFlags&MagicAbility.CAIRN_WANDERER_FLAGS);
		}
	};
    
    public static final MagicChangeCardDefinition SET = new MagicChangeCardDefinition() {
        @Override
        public void change(MagicCardDefinition cdef) {
		    cdef.addLocalVariable(MagicStaticLocalVariable.getInstance());
		    cdef.addLocalVariable(CAIRN_WANDERER);
        }
    };
}
