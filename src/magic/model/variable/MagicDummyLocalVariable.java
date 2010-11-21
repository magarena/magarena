package magic.model.variable;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;

public class MagicDummyLocalVariable implements MagicLocalVariable {
	
	@Override
	public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {

	}

	@Override
	public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {

		return flags;
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