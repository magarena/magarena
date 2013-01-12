package magic.model.event;

import magic.model.MagicCardDefinition;
import magic.model.MagicManaCost;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.MagicCopyable;
import magic.model.MagicCopyMap;
import magic.model.MagicCounterType;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicUntapAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicSingleActivationCondition;
import magic.model.stack.MagicAbilityOnStack;

public abstract class MagicPermanentActivation extends MagicActivation<MagicPermanent> implements MagicChangeCardDefinition, MagicCopyable {
    
    public MagicPermanentActivation(
            final MagicCondition[] conditions,
            final MagicActivationHints hints,
            final String txt) {
        super(conditions,hints,txt);
    }

    @Override
    public final boolean usesStack() {
        return true;
    }

    @Override
    public final MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            this,
            EVENT_ACTION,
            "Play activated ability of SN."
        );
    }

    @Override
    public MagicCopyable copy(final MagicCopyMap copyMap) {
        return this;
    }
    
    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public final void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final MagicPermanentActivation permanentActivation = event.getRefPermanentActivation();
            final MagicPermanent permanent = event.getPermanent();
            final MagicAbilityOnStack abilityOnStack = new MagicAbilityOnStack(
                permanentActivation,
                permanent,
                game.getPayedCost()
            );
            game.doAction(new MagicPutItemOnStackAction(abilityOnStack));
        }
    };

    @Override
    public final MagicTargetChoice getTargetChoice(final MagicPermanent source) {
        return getPermanentEvent(source,MagicPayedCost.NO_COST).getTargetChoice();
    }
   
    public abstract MagicEvent[] getCostEvent(final MagicPermanent source);

    public abstract MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost);
    
    @Override
    public void executeEvent(
            final MagicGame game, 
            final MagicEvent event, 
            final Object[] choiceResults) {
        throw new RuntimeException(getClass() + " did not override executeEvent");
    }
    
    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addAct(this);
    }
    
    public static final MagicPermanentActivation TapAddCharge = new MagicPermanentActivation(
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Pump),
            "Charge") {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{new MagicTapEvent(source)};
        }

        @Override
        public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    this,
                    "Put a charge counter on SN.");
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicChangeCountersAction(
                        event.getPermanent(),
                        MagicCounterType.Charge,
                        1,
                        true));
        }        
    };
    
    public static final MagicPermanentActivation Untap(final MagicManaCost cost) {
        return new MagicPermanentActivation(
            new MagicCondition[]{
                MagicCondition.TAPPED_CONDITION,
                cost.getCondition(),
                new MagicSingleActivationCondition()
            },
            new MagicActivationHints(MagicTiming.Tapping),
            "Untap"
        ) {
            @Override
            public MagicEvent[] getCostEvent(final MagicPermanent source) {
                return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.TWO_BLUE)};
            }
            @Override
            public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
                return new MagicEvent(
                        source,
                        this,
                        "Untap SN.");
            }
            @Override
            public void executeEvent(
                    final MagicGame game,
                    final MagicEvent event,
                    final Object[] choiceResults) {
                game.doAction(new MagicUntapAction(event.getPermanent()));
            }
        };
    }
}
