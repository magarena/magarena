package magic.model.variable;

import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;

public class MagicAttachmentLocalVariable extends MagicDummyLocalVariable {

	private final MagicCardDefinition cardDefinition;

	public MagicAttachmentLocalVariable(final MagicCardDefinition aCardDefinition) {
		cardDefinition = aCardDefinition;
	}
	
	@Override
	public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
        //to be removed, temp fix to get the program to compile
		pt.add(cardDefinition.genPowerToughness(game,game.getPlayer(0)));
	}

	@Override
	public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
		return flags|cardDefinition.getGivenAbilityFlags();
	}
}
