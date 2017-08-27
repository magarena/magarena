package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicSource;
import magic.model.MagicCardDefinition;
import magic.model.MagicLocationType;
import magic.model.ARG;
import magic.model.action.PutItemOnStackAction;
import magic.model.choice.MagicChoice;
import magic.model.condition.MagicCondition;
import magic.model.stack.MagicAbilityOnStack;

import java.util.List;
import java.util.LinkedList;

public abstract class MagicCardAbilityActivation extends MagicHandCastActivation {

    final String name;

    public MagicCardAbilityActivation(final MagicCondition[] conditions, final MagicActivationHints hints, final String aName) {
        super(
            conditions,
            hints,
            aName
        );
        name = aName;
    }

    public MagicCardAbilityActivation(final MagicActivationHints hints, final String aName) {
        this(MagicActivation.NO_COND, hints, aName);
    }

    public abstract MagicEvent getCardEvent(final MagicCard card, final MagicPayedCost payedCost);

    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            this::putOnStack,
            "Play activated ability of SN."
        );
    }

    private void putOnStack(final MagicGame game, final MagicEvent event) {
        final MagicAbilityOnStack abilityOnStack = new MagicAbilityOnStack(
            this,
            getCardEvent(event.getCard(), game.getPayedCost())
        );
        game.doAction(new PutItemOnStackAction(abilityOnStack));
    }

    @Override
    final MagicChoice getChoice(final MagicCard source) {
        return getCardEvent(source, MagicPayedCost.NO_COST).getChoice();
    }

    public static final MagicCardAbilityActivation create(final String act, final MagicLocationType loc) {
        final String[] token = act.split(ARG.COLON, 2);

        // build the actual costs
        final String costs = token[0];
        final List<MagicMatchedCostEvent> matchedCostEvents = MagicRegularCostEvent.build(costs);
        assert matchedCostEvents.size() > 0;

        // add restriction as a MagicMatchedCostEvent
        final String[] part = token[1].split(ActivationRestriction);
        if (part.length > 1) {
            matchedCostEvents.addAll(MagicCondition.build(part[1]));
        }

        // parse the effect
        final String rule = part[0];
        final MagicSourceEvent sourceEvent = MagicRuleEventAction.create(rule);

        boolean isIndependent = sourceEvent.isIndependent();
        for (final MagicMatchedCostEvent matched : matchedCostEvents) {
            isIndependent &= matched.isIndependent();
        }

        return new MagicCardAbilityActivation(
            sourceEvent.getConditions(),
            new MagicActivationHints(
                sourceEvent.getTiming(),
                isIndependent
            ),
            sourceEvent.getName()
        ) {
            @Override
            public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
                return MagicMatchedCostEvent.getCostEvent(matchedCostEvents, source);
            }

            @Override
            public MagicEvent getCardEvent(final MagicCard source, final MagicPayedCost payedCost) {
                return sourceEvent.getEvent(source, payedCost);
            }

            @Override
            public void change(final MagicCardDefinition cdef) {
                if (loc == MagicLocationType.OwnersHand) {
                    cdef.addHandAct(this);
                } else if (loc == MagicLocationType.Graveyard) {
                    cdef.addGraveyardAct(this);
                } else {
                    throw new RuntimeException("unknown location: \"" + loc + "\"");
                }
            }
        };
    }
}
