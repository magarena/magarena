package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPlayer;

import java.util.Collection;
import java.util.Collections;

public abstract class MagicTargetPicker {

	protected abstract int getTargetScore(final MagicGame game,final MagicPlayer player,final Object target);
	
	public Collection<Object> pickTargets(final MagicGame game,final MagicPlayer player,final Collection<Object> options) {

		if (options.size()<2) {
			return options;
		}

		Object bestTarget=null;
		int bestScore=Integer.MIN_VALUE;
		for (final Object target : options) {
			
			final int score=getTargetScore(game,player,target);
			if (score>bestScore) {
				bestTarget=target;
				bestScore=score;
			}
		}
		
		return Collections.singletonList(bestTarget);
	}
}