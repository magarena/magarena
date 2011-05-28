package magic.model.variable;

import magic.model.MagicColoredType;
import magic.model.MagicGame;
import magic.model.MagicColor;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;

/**
 * Implements the following static ability:
 * As long as you control another multicolored permanent, XXX gets +1/+1 and has YYY ability
 * Needed by Bant Sureblade, Esper Stormblade, Grixis Grimblade, Jund Hackblade, Naya Hushblade
 */
public class MagicBladeLocalVariable implements MagicLocalVariable {

	private final long abilityMask;
	
	public MagicBladeLocalVariable(final long abilityMask) {
		
		this.abilityMask=abilityMask;
	}
	
	private boolean isValid(final MagicPermanent owner) {
		
		for (final MagicPermanent permanent : owner.getController().getPermanents()) {
			
			if (permanent!=owner && MagicColor.isMulti(permanent.getColorFlags())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
		
		if (isValid(permanent)) {
			pt.power++;
			pt.toughness++;
		}
	}
	
	@Override
	public long getAbilityFlags(MagicGame game,MagicPermanent permanent,long flags) {

		return isValid(permanent)?flags|abilityMask:flags;
	}
	
	@Override
	public int getSubTypeFlags(final MagicPermanent permanent,final int flags) {

		return flags;
	}

	@Override
	public int getColorFlags(final MagicPermanent permanent,final int flags) {

		return flags;
	}
}
