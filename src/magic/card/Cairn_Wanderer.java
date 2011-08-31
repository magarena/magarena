package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.variable.MagicDummyLocalVariable;
import magic.model.variable.MagicLocalVariable;
import magic.model.variable.MagicStaticLocalVariable;

public class Cairn_Wanderer {
	private static final long CAIRN_WANDERER_FLAGS=
		MagicAbility.Flying.getMask()|
		MagicAbility.Fear.getMask()|
		MagicAbility.FirstStrike.getMask()|
		MagicAbility.DoubleStrike.getMask()|
		MagicAbility.Deathtouch.getMask()|
		MagicAbility.Haste.getMask()|
		MagicAbility.LifeLink.getMask()|
		MagicAbility.Reach.getMask()|		
		MagicAbility.Trample.getMask()|
		MagicAbility.Shroud.getMask()|
		MagicAbility.Vigilance.getMask()|
		MagicAbility.LANDWALK_FLAGS|
		MagicAbility.PROTECTION_FLAGS;

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
			return flags|(newFlags & CAIRN_WANDERER_FLAGS);
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
