package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class Control_Magic {
    public static final Object S = new MagicStatic(MagicLayer.Control) {
        @Override
        public MagicPlayer getController(
                final MagicPermanent source,
                final MagicPermanent target,
                final MagicPlayer player) {
            return source.getController();
        }
        @Override
        public boolean accept(
                final MagicGame game,
                final MagicPermanent source,
                final MagicPermanent target) {
            return source.getEnchantedCreature() == target;
        }
    };
}
