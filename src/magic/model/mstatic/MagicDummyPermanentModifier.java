package magic.model.mstatic;

import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.MagicSubType;
import magic.model.MagicAbility;

import java.util.EnumSet;
import java.util.Set;

public class MagicDummyPermanentModifier implements MagicPermanentModifier {
    
    @Override
    public MagicPlayer getController(final MagicPermanent source, final MagicPermanent permanent, final MagicPlayer controller) {
        return controller;
    }
    
    @Override
    public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
        //leave power and toughness unchanged
    }

    @Override
    public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
        //leave abilities unchanged
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
