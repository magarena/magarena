package magic.model.variable;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.MagicSubType;

import java.util.EnumSet;

// Determines variable power, toughness, abilities, sub types and colors for a single creature permanent.
public interface MagicLocalVariable {

	public abstract void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt);
	
	public abstract long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags);

	public abstract EnumSet<MagicSubType> getSubTypeFlags(final MagicPermanent permanent,final EnumSet<MagicSubType> flags);
	
    public abstract int getTypeFlags(final MagicPermanent permanent,final int flags);
	
	public abstract int getColorFlags(final MagicPermanent permanent,final int flags);	
}
