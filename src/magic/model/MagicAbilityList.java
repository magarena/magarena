package magic.model;

import magic.model.event.MagicActivation;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicPermanentActivation;
import magic.model.trigger.MagicTrigger;
import magic.model.mstatic.MagicStatic;
import magic.model.action.AddTriggerAction;
import magic.model.action.AddStaticAction;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MagicAbilityList implements MagicAbilityStore {

    private List<MagicAbility> abilities = new LinkedList<>();
    private List<MagicTrigger<?>> triggers = new LinkedList<>();
    private List<MagicStatic> statics = new LinkedList<>();
    private List<MagicActivation<MagicPermanent>> permActivations = new LinkedList<>();
    private List<MagicManaActivation> manaActivations = new LinkedList<>();

    @Override
    public void add(final MagicChangeCardDefinition ccd) {
        if (ccd instanceof MagicPermanentActivation) {
            permActivations.add((MagicPermanentActivation)ccd);
        } else if (ccd instanceof MagicManaActivation) {
            manaActivations.add((MagicManaActivation)ccd);
        } else if (ccd instanceof MagicTrigger<?>) {
            triggers.add((MagicTrigger<?>)ccd);
        } else if (ccd instanceof MagicStatic) {
            statics.add((MagicStatic)ccd);
        } else {
            throw new RuntimeException("unknown given ability \"" + ccd + "\"");
        }
    }

    public MagicAbility getFirst() {
        return abilities.get(0);
    }

    @Override
    public void addAbility(final MagicAbility ability) {
        abilities.add(ability);
    }

    public void giveAbility(final MagicGame game, final MagicPermanent source, final MagicPlayer player) {
        final MagicPermanent emblem = game.createPermanent(source.getCard(), player);
        for (final MagicTrigger<?> t : triggers) {
            game.doAction(AddTriggerAction.Force(emblem, t));
        }
        for (final MagicStatic s : statics) {
            game.doAction(AddStaticAction.Force(emblem, s));
        }
    }

    public void giveAbility(final MagicPermanent permanent, final Set<MagicAbility> flags) {
        flags.addAll(abilities);

        for (final MagicActivation<MagicPermanent> permAct : permActivations) {
            permanent.addAbility(permAct);
        }
        for (final MagicManaActivation manaAct : manaActivations) {
            permanent.addAbility(manaAct);
        }
        for (final MagicTrigger<?> trigger : triggers) {
            permanent.addAbility(trigger);
        }
    }

    public void loseAbility(final MagicPermanent permanent, final Set<MagicAbility> flags) {
        flags.removeAll(abilities);
    }
}
