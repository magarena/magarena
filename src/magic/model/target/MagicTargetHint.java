package magic.model.target;

import magic.model.MagicPlayer;

public enum MagicTargetHint {
	Positive,
	Negative,
	None;
	
	public boolean accept(final MagicPlayer player,final MagicTarget target) {
		switch (this) {
			case Positive:
				return target.getController()==player;
			case Negative:
				return target.getController()!=player;
			default:
				return true;		
		}
	}
}
