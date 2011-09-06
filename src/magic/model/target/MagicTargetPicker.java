package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPlayer;

import java.util.Collection;
import java.util.Collections;

public abstract class MagicTargetPicker<T> {

	protected abstract int getTargetScore(final MagicGame game,final MagicPlayer player,final T target);
	
	public Collection<T> pickTargets(final MagicGame game,final MagicPlayer player,final Collection<T> options) {
		if (options.size()<2) {
			return options;
		}
		
        T bestTarget=options.iterator().next();
		int bestScore=Integer.MIN_VALUE;
		for (final T target : options) {
			final int score=getTargetScore(game,player,target);
			if (score>bestScore) {
				bestTarget=target;
				bestScore=score;
			}
		}
		
		return Collections.singletonList(bestTarget);
	}
}
