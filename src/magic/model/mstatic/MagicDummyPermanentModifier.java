package magic.model.mstatic;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.MagicSubType;

import java.util.EnumSet;

public class MagicDummyPermanentModifier implements MagicPermanentModifier {
    
    @Override
    public MagicPlayer getController(final MagicGame game, final MagicPermanent permanent) {
        return permanent.getController();
    }
	
	@Override
	public void getPowerToughness(final MagicGame game, final MagicPermanent permanent, final MagicPowerToughness pt) {
        //leave power and toughness unchanged
	}

	@Override
	public long getAbilityFlags(final MagicGame game, final MagicPermanent permanent, final long flags) {
		return flags;
	}

	@Override
	public EnumSet<MagicSubType> getSubTypeFlags(final MagicPermanent permanent, final EnumSet<MagicSubType> flags) {
		return flags;
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
