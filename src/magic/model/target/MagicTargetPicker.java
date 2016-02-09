package magic.model.target;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;

public abstract class MagicTargetPicker<T> {

    protected void setEvent(final MagicEvent event) {
        //do nothing
    }

    protected abstract int getTargetScore(final MagicGame game,final MagicPlayer event,final T target);

    public Collection<T> pickTargets(final MagicGame game,final MagicEvent event,final Collection<T> options) {
        if (options.size()<2) {
            return options;
        }

        T bestTarget=options.iterator().next();
        int bestScore=Integer.MIN_VALUE;
        setEvent(event);
        for (final T target : options) {
            final int score=getTargetScore(game,event.getPlayer(),target);
            if (score>bestScore) {
                bestTarget=target;
                bestScore=score;
            }
        }

        return Collections.singletonList(bestTarget);
    }

    private static final Map<String, MagicTargetPicker<MagicPermanent>> factory =
        new HashMap<String, MagicTargetPicker<MagicPermanent>>();

    static {
        register("pump", MagicPumpTargetPicker.create());
        register("weaken", new MagicWeakenTargetPicker(0,0));
        register("flying", MagicFlyingTargetPicker.create());
        register("lifelink", MagicLifelinkTargetPicker.create());
        register("copy", MagicCopyPermanentPicker.create());
        register("haste", MagicHasteTargetPicker.create());
        register("trample", MagicTrampleTargetPicker.create());
        register("unblockable", MagicUnblockableTargetPicker.create());
        register("first strike", MagicFirstStrikeTargetPicker.create());
        register("sacrifice", MagicSacrificeTargetPicker.create());
        register("destroy", MagicDestroyTargetPicker.Destroy);
        register("destroy no regen", MagicDestroyTargetPicker.DestroyNoRegen);
        register("shroud", MagicShroudTargetPicker.create());
        register("can't attack or block", new MagicNoCombatTargetPicker(true,true,true));
        register("can't attack", new MagicNoCombatTargetPicker(true,false,true));
        register("can't block", new MagicNoCombatTargetPicker(false,true,true));
        register("defender", new MagicNoCombatTargetPicker(true,false,true));
        register("tap", new MagicNoCombatTargetPicker(true,true,false));
        register("untap", MagicTapTargetPicker.Untap);
        register("indestructible", MagicIndestructibleTargetPicker.create());
        register("must attack", MagicMustAttackTargetPicker.create());
        register("lose flying", MagicLoseFlyingTargetPicker.create());
        register("gain control", MagicExileTargetPicker.create());
        register("exile", MagicExileTargetPicker.create());
        register("power", MagicPowerTargetPicker.create());
        register("toughness", MagicToughnessTargetPicker.create());
        register("default", MagicDefaultPermanentTargetPicker.create());
        register("regen", MagicRegenerateTargetPicker.create());
    }

    public MagicTargetPicker<MagicPermanent> create(final String arg) {
        throw new UnsupportedOperationException();
    }

    public static MagicTargetPicker<MagicPermanent> build(final String arg) {
        if (factory.containsKey(arg)) {
            return factory.get(arg);
        } else {
            final String[] args = arg.split(" ", 2);
            return factory.get(args[0]).create(args[1]);
        }
    }

    public static void register(final String key, final MagicTargetPicker<MagicPermanent> tp) {
        factory.put(key, tp);
    }
}
