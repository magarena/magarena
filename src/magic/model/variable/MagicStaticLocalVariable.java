package magic.model.variable;

import magic.data.CardDefinitions;
import magic.model.MagicAbility;
import magic.model.MagicColor;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.mstatic.MagicStatic;
import magic.model.mstatic.MagicPermanentStatic;
import magic.model.mstatic.MagicPermanentStaticList;

import java.util.EnumSet;

// Implements all static abilities of cards.
public class MagicStaticLocalVariable extends MagicDummyLocalVariable {

	private static final MagicLocalVariable INSTANCE=new MagicStaticLocalVariable();
    
    public static final MagicLocalVariable getInstance() {
		return INSTANCE;
	}
	
    // You can't be target of spells or abilities your opponent controls.
	private static int spiritOfTheHearth; 
	
    public static void initializeCardDefinitions() {
		final CardDefinitions definitions=CardDefinitions.getInstance();
		spiritOfTheHearth=definitions.getCard("Spirit of the Hearth").getIndex();
	}
	
    public static boolean canTarget(final MagicPlayer controller) {
        return controller.getCount(spiritOfTheHearth) == 0;
    }
	
    @Override
	public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
        for (final MagicPermanentStatic mpstatic : game.getStatics()) {
            final MagicStatic mstatic = mpstatic.getStatic();
            if (mstatic.accept(game, mpstatic.getPermanent(),permanent)) {
                mstatic.getPowerToughness(game, permanent, pt);
            }
        }
    }
	
	@Override
	public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,long flags) {
        for (final MagicPermanentStatic mpstatic : game.getStatics()) {
            final MagicStatic mstatic = mpstatic.getStatic();
            if (mstatic.accept(game, mpstatic.getPermanent(), permanent)) {
                flags = mstatic.getAbilityFlags(game, permanent, flags);
            }
        }
		return flags;
	}

	@Override
	public EnumSet<MagicSubType> getSubTypeFlags(final MagicPermanent permanent,final EnumSet<MagicSubType> flags) {
		if (permanent.getCardDefinition().hasAbility(MagicAbility.Changeling)) {
            final EnumSet<MagicSubType> mod = flags.clone();
            mod.addAll(MagicSubType.ALL_CREATURES);
			return mod;
		}		
		return flags;
	}
}
