package magic.model.action;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import magic.model.MagicAbility;
import magic.model.MagicAbilityList;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

// Set an ability until end of turn.
public class LoseAbilityAction extends MagicAction {

    private final MagicPermanent permanent;
    private final MagicAbilityList abilityList;
    private final boolean duration;

    public LoseAbilityAction(final MagicPermanent permanent,final MagicAbilityList abilityList, final boolean duration) {
        this.permanent=permanent;
        this.abilityList=abilityList;
        this.duration=duration;
    }

    public LoseAbilityAction(final MagicPermanent permanent,final Set<MagicAbility> abilities, final boolean duration) {
        this(permanent,MagicAbility.getAbilityList(abilities), duration);
    }

    public LoseAbilityAction(final MagicPermanent permanent,final MagicAbilityList abilityList) {
        this(permanent,abilityList,MagicStatic.UntilEOT);
    }

    public LoseAbilityAction(final MagicPermanent permanent,final List<MagicAbility> abilities) {
        this(permanent,EnumSet.copyOf(abilities),MagicStatic.UntilEOT);
    }

    public LoseAbilityAction(final MagicPermanent permanent,final MagicAbility first, final MagicAbility ... rest) {
        this(permanent,MagicAbility.of(first,rest),MagicStatic.UntilEOT);
    }

    public LoseAbilityAction(final MagicPermanent permanent,final MagicAbility ability,final boolean duration) {
        this(permanent,MagicAbility.of(ability),duration);
    }

    public LoseAbilityAction(final MagicPermanent permanent,final MagicAbility ability) {
        this(permanent,MagicAbility.of(ability),MagicStatic.UntilEOT);
    }

    @Override
    public void doAction(final MagicGame game) {
        game.doAction(new AddStaticAction(permanent, new MagicStatic(MagicLayer.Ability, duration) {
            @Override
            public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
                abilityList.loseAbility(permanent, flags);
            }
        }));
    }

    @Override
    public void undoAction(final MagicGame game) {
    }

    @Override
    public String toString() {
        return super.toString() + " (" + permanent + "," + abilityList + ')';
    }
}
