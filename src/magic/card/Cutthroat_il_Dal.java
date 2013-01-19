package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicPermanent;
import magic.model.condition.MagicCondition;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

import java.util.Set;

public class Cutthroat_il_Dal {
    public static final MagicStatic S = new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (MagicCondition.HELLBENT.accept(permanent)) {
                flags.add(MagicAbility.Shadow);
            }
        }
    };
}
