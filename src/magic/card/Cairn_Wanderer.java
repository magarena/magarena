package magic.card;

import magic.model.*;
import magic.model.variable.MagicDummyLocalVariable;
import magic.model.variable.MagicLocalVariable;
import magic.model.variable.MagicStaticLocalVariable;

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
