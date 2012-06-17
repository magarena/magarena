package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.mstatic.MagicLayer;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

public class Joraga_Warcaller {
	public static final MagicStatic S = new MagicStatic(
			MagicLayer.ModPT, 
			MagicTargetFilter.TARGET_ELF_YOU_CONTROL) {

		private int amount = 0;

        @Override
        public void setSource(final MagicPermanent source) {
            amount = source.getCounters(MagicCounterType.PlusOne);
        }

		@Override
		public void modPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.add(amount, amount);
		}
		@Override
		public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
			return source != target;
		}
	};
}
