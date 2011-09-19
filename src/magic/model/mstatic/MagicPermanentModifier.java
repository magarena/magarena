package magic.model.mstatic;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.MagicSubType;

import java.util.EnumSet;

// Determines variable power, toughness, abilities, sub types and colors for a single creature permanent.
public interface MagicPermanentModifier {

	void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt);
	
	long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags);

	EnumSet<MagicSubType> getSubTypeFlags(final MagicPermanent permanent,final EnumSet<MagicSubType> flags);
	
    int getTypeFlags(final MagicPermanent permanent,final int flags);
	
	int getColorFlags(final MagicPermanent permanent,final int flags);	
}
