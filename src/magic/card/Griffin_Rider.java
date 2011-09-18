package magic.card;

import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.MagicSubType;
import magic.model.MagicAbility;
import magic.model.variable.MagicDummyLocalVariable;
import magic.model.variable.MagicLocalVariable;

public class Griffin_Rider {
	private static boolean isValid(final MagicPermanent owner,final MagicGame game) {
		for (final MagicPermanent permanent : owner.getController().getPermanents()) {
			if (permanent != owner && permanent.hasSubType(MagicSubType.Griffin,game)) {
				return true;
			}
		}
		return false;
	}

	public static final MagicLocalVariable GRIFFIN_RIDER = new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			if (isValid(permanent,game)) {
				pt.add(3,3);
			}
		}	
        @Override
        public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return (isValid(permanent,game)) ?
                flags | MagicAbility.Flying.getMask() :
                flags;
        }
	};
}
