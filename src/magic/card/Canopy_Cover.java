package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicAbility;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class Canopy_Cover {
    public static final Object S = new MagicStatic(MagicLayer.Ability) {
        @Override
        public long getAbilityFlags(
            final MagicPermanent source,
            final MagicPermanent target,
            long flags) {
            return flags | 
                ((source.getController().getIndex() == 0) ?
                 MagicAbility.CannotBeTheTarget1 :
                 MagicAbility.CannotBeTheTarget0).getMask();
        }
        @Override
        public boolean accept(
                final MagicGame game,
                final MagicPermanent source,
                final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target);
        }
    };
}
