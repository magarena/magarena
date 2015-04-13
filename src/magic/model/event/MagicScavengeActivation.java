package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.ChangeCountersAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.stack.MagicAbilityOnStack;
import magic.model.target.MagicPumpTargetPicker;

import java.util.Arrays;

public class MagicScavengeActivation extends MagicGraveyardActivation {

    private static final MagicCondition[] COND = new MagicCondition[]{ MagicCondition.SORCERY_CONDITION };
    private static final MagicActivationHints HINT = new MagicActivationHints(MagicTiming.Pump,true);
    final MagicManaCost cost;
    final MagicCardDefinition cdef;

    public MagicScavengeActivation(final MagicCardDefinition aCdef, final MagicManaCost aCost) {
        super(
            COND,
            HINT,
            "Scavenge"
        );
        cost = aCost;
        cdef = aCdef;
    }

    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return Arrays.asList(
            new MagicPayManaCostEvent(source, cost),
            new MagicExileSelfEvent(source, MagicLocationType.Graveyard)
        );
    }

    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final MagicAbilityOnStack abilityOnStack = new MagicAbilityOnStack(
                        MagicScavengeActivation.this,
                        getCardEvent(event.getCard(), game.getPayedCost())
                    );
                    game.doAction(new MagicPutItemOnStackAction(abilityOnStack));
                }
            },
            "Scavenge SN."
        );
    }

    public MagicEvent getCardEvent(final MagicCard source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            MagicTargetChoice.POS_TARGET_CREATURE,
            MagicPumpTargetPicker.create(),
            cdef.getCardPower(),
            this,
            "Put RN +1/+1 counters on target creature$."
        );
    }
    
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        event.processTargetPermanent(game, new MagicPermanentAction() {
            public void doAction(final MagicPermanent perm) {
                game.doAction(new ChangeCountersAction(
                    perm,
                    MagicCounterType.PlusOne,
                    event.getRefInt()
                ));
            }
        });
    }

    @Override
    final MagicChoice getChoice(final MagicCard source) {
        return getCardEvent(source,MagicPayedCost.NO_COST).getChoice();
    }
}
