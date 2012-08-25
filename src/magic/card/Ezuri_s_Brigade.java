package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicAbility;
import magic.model.MagicPowerToughness;
import magic.model.MagicType;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class Ezuri_s_Brigade {
    public static final MagicStatic S1 = new MagicStatic(MagicLayer.Ability) {
        @Override
        public long getAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final long flags) {
            return permanent.getController().getNrOfPermanentsWithType(MagicType.Artifact) >= 3 ?
                flags | MagicAbility.Trample.getMask() :
                flags;
        }
    };
    
    public static final MagicStatic S2 = new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
            if (permanent.getController().getNrOfPermanentsWithType(MagicType.Artifact) >= 3) {
                pt.add(4,4);
            }
        }
    };
}
