package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicPumpTargetPicker;


public class MagicModularTrigger extends MagicWhenPutIntoGraveyardTrigger {

    private static final MagicModularTrigger INSTANCE = new MagicModularTrigger();

    private MagicModularTrigger() {}

    public static MagicModularTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicGraveyardTriggerData triggerData) {
        if (triggerData.fromLocation == MagicLocationType.Play) {
            final int amount = permanent.getCounters(MagicCounterType.PlusOne);
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    amount > 1 ?
                        "You may put " + amount + " +1/+1 counters on target artifact creature." :
                        "You may put a +1/+1 counter on target artifact creature.",
                    MagicTargetChoice.POS_TARGET_ARTIFACT_CREATURE
                ),
                MagicPumpTargetPicker.create(),
                amount,
                this,
                amount > 1 ?
                    "PN may$ put " + amount + " +1/+1 counters on target artifact creature$." :
                    "PN may$ put a +1/+1 counter on target artifact creature$."
            );
        }
        return MagicEvent.NONE;
    }
    
    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] choiceResults) {
        if (MagicMayChoice.isYesChoice(choiceResults[0])) {
            event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeCountersAction(
                        creature,
                        MagicCounterType.PlusOne,
                        event.getRefInt(),
                        true
                    ));
                }
            });
        }
    }
}
