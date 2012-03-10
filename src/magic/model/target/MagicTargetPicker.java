package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

public abstract class MagicTargetPicker<T> {

    private static Map<String, MagicTargetPicker<MagicPermanent>> picker = 
        new HashMap<String, MagicTargetPicker<MagicPermanent>>();
    
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

    protected MagicTargetPicker<MagicPermanent> create(String[] args) {
        return null; 
    }

    public static MagicTargetPicker<MagicPermanent> create(String script) {
        String[] args = script.split(" ");
        return picker.get(args[0]).create(args);
    }

    public static void register(String key, MagicTargetPicker<MagicPermanent> tp) {
        picker.put(key, tp);
    }
}
