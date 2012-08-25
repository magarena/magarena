package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicType;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

public class Predator_s_Gambit {
    public static final MagicStatic S = new MagicStatic(
            MagicLayer.Ability, 
            MagicTargetFilter.TARGET_CREATURE) {
        @Override
        public long getAbilityFlags(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final long flags) {
            return flags | MagicAbility.Intimidate.getMask();
        }
        @Override
        public boolean condition(
                final MagicGame game,
                final MagicPermanent source,
                final MagicPermanent target) {
            return target == source.getEnchantedCreature() &&
                    source.getController().getNrOfPermanentsWithType(MagicType.Creature) == 1;
        }
    };
}
