package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSubType;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

public class Butcher_s_Cleaver {
	public static final MagicStatic S = new MagicStatic(
			MagicLayer.Ability, 
			MagicTargetFilter.TARGET_CREATURE) {
		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return flags | MagicAbility.LifeLink.getMask();
		}
		@Override
		public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
			return source.getEquippedCreature().hasSubType(MagicSubType.Human) &&
					target == source.getEquippedCreature();
		}
	};
}
