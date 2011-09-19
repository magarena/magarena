package magic.model.target;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPlayer;

public class MagicGraveyardTargetPicker extends MagicTargetPicker<MagicCard> {

	private final boolean noCastingCost;
	
	public MagicGraveyardTargetPicker(final boolean noCastingCost) {
		this.noCastingCost = noCastingCost;
	}
	

	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicCard target) {
		return noCastingCost ?
			ArtificialScoringSystem.getFreeCardScore(target):
			ArtificialScoringSystem.getCardScore(target);
	}
}
