package magic.model.mstatic;

import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.MagicSubType;
import magic.model.MagicAbility;

import java.util.EnumSet;
import java.util.Set;

// Determines variable power, toughness, abilities, sub types and colors for a single creature permanent.
public interface MagicPermanentModifier {

    MagicPlayer getController(final MagicPermanent source, final MagicPermanent permanent, final MagicPlayer controller); 

    void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt);
    
    void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags);

    void modSubTypeFlags(final MagicPermanent permanent, final EnumSet<MagicSubType> flags);
    
    int getTypeFlags(final MagicPermanent permanent, final int flags);
    
    int getColorFlags(final MagicPermanent permanent, final int flags);    
    
    void modPlayer(final MagicPermanent source, final MagicPermanent permanent);
}
