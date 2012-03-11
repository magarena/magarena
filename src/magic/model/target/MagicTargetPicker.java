package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

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
    
    private static Map<String, MagicTargetPicker<MagicPermanent>> factory = 
        new HashMap<String, MagicTargetPicker<MagicPermanent>>();

    static {
        register("pump", MagicPumpTargetPicker.create());
        register("weaken -13/-0", new MagicWeakenTargetPicker(13,0));
        register("weaken -3/-0", new MagicWeakenTargetPicker(3,0));
        register("weaken -4/-1", new MagicWeakenTargetPicker(4,1));
        register("weaken +2/-2", new MagicWeakenTargetPicker(2,2));
        register("weaken -2/-2", new MagicWeakenTargetPicker(2,2));
        register("weaken -2/-1", new MagicWeakenTargetPicker(2,1));
        register("flying", MagicFlyingTargetPicker.create());
        register("lifelink", MagicLifelinkTargetPicker.create());
        register("copy", MagicCopyTargetPicker.create());
        register("haste", MagicHasteTargetPicker.create());
        register("trample", MagicTrampleTargetPicker.create());
        register("unblockable", MagicUnblockableTargetPicker.create());
        register("first strike", MagicFirstStrikeTargetPicker.create());
        register("sacrifice", MagicSacrificeTargetPicker.create());
        register("destroy", new MagicDestroyTargetPicker(false));
        register("destroy no regen", new MagicDestroyTargetPicker(true));
        register("shroud", MagicShroudTargetPicker.create());
        register("can't attack or block", new MagicNoCombatTargetPicker(true,true,true));
        register("defender", new MagicNoCombatTargetPicker(true,false,true));
        register("tap", new MagicNoCombatTargetPicker(true,true,false));
        register("indestructible", MagicIndestructibleTargetPicker.create());
    }

    public static MagicTargetPicker<MagicPermanent> create(String arg) {
        return factory.get(arg);
    }

    public static void register(String key, MagicTargetPicker<MagicPermanent> tp) {
        factory.put(key, tp);
    }
}
