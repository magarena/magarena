package magic.card;

import magic.model.MagicGame;
import magic.model.mstatic.MagicLayer;
import magic.model.MagicAbility;
import magic.model.MagicPermanent;
import magic.model.mstatic.MagicStatic;

public class Viridian_Betrayers {
    public static final MagicStatic S = new MagicStatic(MagicLayer.Ability) {
        @Override
        public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
            return game.getOpponent(permanent.getController()).getPoison() > 0 ?
                flags | MagicAbility.Infect.getMask() :
                flags;
        }
    };
}
