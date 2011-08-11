package magic.model.event;

import magic.model.*;
import magic.model.condition.MagicCondition;
import magic.model.choice.MagicTargetChoice;
import magic.model.action.MagicTapAction;
import magic.model.target.MagicTapTargetPicker;

public abstract class MagicTapCreatureActivation extends MagicPermanentActivation {
		
	public MagicTapCreatureActivation(
            final MagicCondition[] conds, 
            final MagicActivationHints hints,
            final String text) {
		super(conds,hints,text);
	}

    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return new MagicEvent(
                source,
                source.getController(),
                MagicTargetChoice.NEG_TARGET_CREATURE,
                new MagicTapTargetPicker(true,false),
                MagicEvent.NO_DATA,
                this,
                "Tap target creature$.");
    }

    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] data,
            final Object[] choiceResults) {
        final MagicPermanent creature=event.getTarget(game,choiceResults,0);
        if (creature!=null&&!creature.isTapped()) {
            game.doAction(new MagicTapAction(creature,true));
        }
    }
}
