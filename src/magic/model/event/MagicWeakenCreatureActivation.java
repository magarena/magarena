package magic.model.event;

import magic.model.*;
import magic.model.condition.MagicCondition;
import magic.model.choice.MagicTargetChoice;
import magic.model.action.MagicChangeCountersAction;
import magic.model.target.MagicWeakenTargetPicker;
import magic.model.action.MagicPermanentAction;

public abstract class MagicWeakenCreatureActivation extends MagicPermanentActivation {
		
	public MagicWeakenCreatureActivation(
            final MagicCondition[] conds, 
            final MagicActivationHints hints,
            final String text) {
		super(conds,hints,text);
	}
		
    @Override
    public MagicEvent getPermanentEvent(
            final MagicPermanent source,
            final MagicPayedCost payedCost) {
        return new MagicEvent(
                source,
                source.getController(),
                MagicTargetChoice.NEG_TARGET_CREATURE,
                new MagicWeakenTargetPicker(1,1),
                MagicEvent.NO_DATA,
                this,
                "Put a -1/-1 counter on target creature$.");
    }

    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] data,
            final Object[] choiceResults) {
        event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
            public void doAction(final MagicPermanent creature) {
                game.doAction(new MagicChangeCountersAction(
                            creature,
                            MagicCounterType.MinusOne,
                            1,
                            true));
            }
        });
    }
}
