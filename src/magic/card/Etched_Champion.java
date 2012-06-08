package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicAbility;
import magic.model.MagicType;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class Etched_Champion {
	public static final MagicStatic S = new MagicStatic(MagicLayer.Ability) {
        @Override
        public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return permanent.getController().getNrOfPermanentsWithType(MagicType.Artifact) >= 3 ?
                flags | MagicAbility.ProtectionFromAllColors.getMask() :
                flags;
        }
	};
}
