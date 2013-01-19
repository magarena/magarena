package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicPermanent;
import magic.model.MagicSubType;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

import java.util.Set;

public class Angelic_Overseer {
    public static final MagicStatic S = new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            for (final MagicPermanent target : permanent.getController().getPermanents()) {
                if (target.hasSubType(MagicSubType.Human)) {
                    flags.add(MagicAbility.Hexproof);
                    flags.add(MagicAbility.Indestructible);
                    break;
                }
            }
        }
    };
}
