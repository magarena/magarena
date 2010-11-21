package magic.model.target;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPlayer;

public class MagicGraveyardTargetPicker extends MagicTargetPicker {

	private static final MagicTargetPicker INSTANCE=new MagicGraveyardTargetPicker();
	
	private MagicGraveyardTargetPicker() {
		
	}

	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final Object target) {

		return ArtificialScoringSystem.getCardScore((MagicCard)target);
	}
	
	public static MagicTargetPicker getInstance() {
		
		return INSTANCE;
	}
}