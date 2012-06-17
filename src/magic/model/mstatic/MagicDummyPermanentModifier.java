package magic.model.mstatic;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.MagicSubType;

import java.util.EnumSet;

public class MagicDummyPermanentModifier implements MagicPermanentModifier {
    
    @Override
    public MagicPlayer getController(final MagicPermanent source, final MagicPermanent permanent, final MagicPlayer controller) {
        return controller;
    }
    
    @Override
    public void modPowerToughness(final MagicGame game, final MagicPermanent permanent, final MagicPowerToughness pt) {
        //leave power and toughness unchanged
    }

    @Override
    public long getAbilityFlags(final MagicGame game, final MagicPermanent permanent, final long flags) {
        return flags;
    }

    @Override
    public void modSubTypeFlags(final MagicPermanent permanent, final EnumSet<MagicSubType> flags) {
        //leave subtype unchanged
    }
    
    @Override
    public int getTypeFlags(final MagicPermanent permanent, final int flags) {
        return flags;
    }

    @Override
    public int getColorFlags(final MagicPermanent permanent, final int flags) {
        return flags;
    }    
}
