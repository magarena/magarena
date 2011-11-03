package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicAbility;
import magic.model.MagicType;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class Spiraling_Duelist {
	public static final MagicStatic S = new MagicStatic(MagicLayer.Ability) {
        @Override
        public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return permanent.getController().getNrOfPermanentsWithType(MagicType.Artifact,game) >= 3 ?
                flags | MagicAbility.DoubleStrike.getMask() :
                flags;
        }
	};
}
