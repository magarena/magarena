package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicSource;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicCounterType;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicPumpTargetPicker;

public class MagicPutCounterEvent extends MagicEvent {

    public MagicPutCounterEvent(final MagicSource source, final int amount) {
        super(
            source,
            MagicTargetChoice.POS_TARGET_CREATURE,
            MagicPumpTargetPicker.create(),
            amount,
            EA,
            "PN puts " + amount + " +1/+1 counters on target creature$."
        );
    }
    
    private static final MagicEventAction EA = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
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
    };
}
