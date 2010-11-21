package magic.model.phase;

import magic.model.MagicGame;

public interface MagicGameplay {

	public MagicPhase getStartPhase(final MagicGame game);
	
	public MagicPhase getNextPhase(final MagicGame game);	
}