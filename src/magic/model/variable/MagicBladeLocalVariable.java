package magic.model.variable;

import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;

/**
 * Implements the following static ability:
 * As long as you control another multicolored permanent, XXX gets +1/+1 and has YYY ability
 * Needed by Bant Sureblade, Esper Stormblade, Grixis Grimblade, Jund Hackblade, Naya Hushblade
 */
public class MagicBladeLocalVariable extends MagicDummyLocalVariable {

	private final long abilityMask;
	
	public MagicBladeLocalVariable(final long abilityMask) {
		this.abilityMask=abilityMask;
	}
	
	/**
     * retrieve all of controller's permanents, true if there exist one that
     * is not itself and is multicolored, false otherwise
     */
	private static boolean isValid(final MagicPermanent owner) {
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
            pt.add(1,1);
		}
	}
	
	@Override
	public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
		return isValid(permanent)?flags|abilityMask:flags;
	}
}
