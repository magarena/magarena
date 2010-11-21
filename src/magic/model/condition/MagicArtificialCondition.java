package magic.model.condition;

import magic.model.MagicGame;
import magic.model.MagicSource;

public class MagicArtificialCondition implements MagicCondition {

	private final MagicCondition normalCondition;
	private final MagicCondition artificialCondition;
	
	public MagicArtificialCondition(final MagicCondition normalCondition,final MagicCondition artificialCondition) {
		
		this.normalCondition=normalCondition;
		this.artificialCondition=artificialCondition;
	}

	@Override
	public boolean accept(final MagicGame game,final MagicSource source) {
		
		return game.isArtificial()?artificialCondition.accept(game,source):normalCondition.accept(game,source);
	}
}