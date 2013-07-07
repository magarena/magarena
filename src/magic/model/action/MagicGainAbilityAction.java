package magic.model.action;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

import java.util.Set;
import java.util.EnumSet;
import java.util.List;

// Set an ability until end of turn.
public class MagicGainAbilityAction extends MagicAction {

    private final MagicPermanent permanent;
    private final Set<MagicAbility> abilities;
    private final boolean duration;

    public MagicGainAbilityAction(final MagicPermanent permanent,final Set<MagicAbility> abilities, final boolean duration) {
        this.permanent=permanent;
        this.abilities=abilities;
        this.duration=duration;
    }

    public MagicGainAbilityAction(final MagicPermanent permanent,final List<MagicAbility> abilities) {
        this(permanent,EnumSet.copyOf(abilities),MagicStatic.UntilEOT);
    }

    public MagicGainAbilityAction(final MagicPermanent permanent,final MagicAbility first, final MagicAbility ... rest) {
        this(permanent,MagicAbility.of(first,rest),MagicStatic.UntilEOT);
    }

    public MagicGainAbilityAction(final MagicPermanent permanent,final MagicAbility ability,final boolean duration) {
        this(permanent,MagicAbility.of(ability),duration);
    }

    public MagicGainAbilityAction(final MagicPermanent permanent,final MagicAbility ability) {
        this(permanent,MagicAbility.of(ability),MagicStatic.UntilEOT);
    }

    @Override
    public void doAction(final MagicGame game) {
        game.doAction(new MagicAddStaticAction(permanent, new MagicStatic(
                MagicLayer.Ability,
                duration) {
            @Override
            public void modAbilityFlags(
                    final MagicPermanent source,
                    final MagicPermanent permanent,
                    final Set<MagicAbility> flags) {
                flags.addAll(abilities);
            }
        }));
    }

    @Override
    public void undoAction(final MagicGame game) {
    }

    @Override
    public String toString() {
        return super.toString() + " (" + permanent + "," + abilities + ')';
    }
}
