package magic.model.event;

import magic.data.GeneralConfig;
import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerState;
import magic.model.MagicSource;
import magic.model.stack.MagicItemOnStack;
import magic.model.choice.MagicChoice;
import magic.model.condition.MagicCondition;

public abstract class MagicActivation<T extends MagicSource> implements MagicEventAction, Comparable<MagicActivation<?>> {

    public static final MagicCondition[] NO_COND = new MagicCondition[0];
    protected static final String ActivationRestriction = " Activate this ability ";

    private final int priority;
    private final long id;
    private final String text;
    private final MagicCondition[] conditions;
    private final MagicActivationHints hints;

    MagicActivation(final MagicCondition[] conditions, final MagicActivationHints hints, final String txt) {
        this.text = txt;
        this.conditions=conditions;
        this.hints=hints;
        this.priority=hints.getTiming().getPriority();

        //randomly assigned, used for ordering activations
        this.id = hashCode();
    }

    public final MagicActivationHints getActivationHints() {
        return hints;
    }

    public final String getText() {
        return text;
    }

    private final boolean checkActivationPriority(final MagicSource source) {
        final MagicActivationPriority actpri = source.getController().getActivationPriority();
        final int priorityDif = priority - actpri.getPriority();
        if (priorityDif > 0) {
            return true;
        } else if (priorityDif < 0) {
            return false;
        }
        return id >= actpri.getActivationId();
    }

    void changeActivationPriority(final MagicPlayer player) {
        final MagicActivationPriority actpri = player.getActivationPriority();
        actpri.setPriority(priority);
        actpri.setActivationId(id);

        // reset activation/priority of opponent
        player.getOpponent().getActivationPriority().clear();
    }

    public boolean canPlay(final MagicGame game, final MagicPlayer player, final T source, final boolean isAI) {

        if (isAI && game.getHintPriority() && !checkActivationPriority(source)) {
            return false;
        }

        if (isAI && game.getHintTiming() && !hints.getTiming().canPlay(game, source)) {
            return false;
        }

        if (source.isPermanent() && ((MagicPermanent)source).hasAbility(MagicAbility.CantActivateAbilities)) {
            return false;
        }

        if (source.isPermanent() && player.hasState(MagicPlayerState.CantActivateAbilities)) {
            return false;
        }

        if (source.isSpell() && player.hasState(MagicPlayerState.CantCastSpells)) {
            return false;
        }

        // Handling of split second
        for (final MagicItemOnStack item : game.getStack()) {
            if (item.hasAbility(MagicAbility.SplitSecond)) {
                return false;
            }
        }

        // Check conditions for activation
        for (final MagicCondition condition : conditions) {
            if (!condition.accept(source)) {
                return false;
            }
        }

        // Check able to pay costs
        //
        // Current method of using MagicCondition could lead to unpayable costs in certain cases:
        // * self needed to tap to pay mana cost, but self must tap as part of cost
        // Ideally execute each event with the first choice, if all events have choices then cost can be paid
        // * execute tap self event
        // * execute pay mana cost event
        //
        for (final MagicEvent event : getCostEvent(source)) {
            if (!event.isSatisfied()) {
                return false;
            }
        }

        // Check for options for choice
        final boolean useTargetHints = (isAI && game.getHintTarget()) || GeneralConfig.getInstance().getSmartTarget();
        final MagicChoice choice = getChoice(source);
        return choice.hasOptions(game, player, source, useTargetHints);
    }

    @Override
    public int compareTo(final MagicActivation<?> other) {
        return Long.signum(id-other.id);
    }

    abstract boolean usesStack();

    abstract Iterable<? extends MagicEvent> getCostEvent(final T source);

    public abstract MagicEvent getEvent(final MagicSource source);

    abstract MagicChoice getChoice(final T source);
}
