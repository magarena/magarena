package magic.model.variable;

import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;

public class MagicAttachmentLocalVariable extends MagicDummyLocalVariable {

	private final MagicCardDefinition cardDefinition;
	
	public MagicAttachmentLocalVariable(final MagicCardDefinition cardDefinition) {
		this.cardDefinition=cardDefinition;
	}
	
	@Override
	public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
		pt.add(cardDefinition.genPowerToughness(game));
	}

	@Override
	public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
		return flags|cardDefinition.getGivenAbilityFlags();
	}
}
