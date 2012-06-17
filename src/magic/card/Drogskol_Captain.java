package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

public class Drogskol_Captain {
	public static final MagicStatic S1 = new MagicStatic(
			MagicLayer.ModPT, 
			MagicTargetFilter.TARGET_SPIRIT_YOU_CONTROL) {
		@Override
		public void modPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.add(1,1);
		}
		@Override
		public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
			return source != target;
		}
	};

	public static final MagicStatic S2 = new MagicStatic(
			MagicLayer.Ability, 
			MagicTargetFilter.TARGET_SPIRIT_YOU_CONTROL) {
		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return flags | MagicAbility.CannotBeTheTarget.getMask();
		}
		@Override
		public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
			return source != target;
		}
	};
}
