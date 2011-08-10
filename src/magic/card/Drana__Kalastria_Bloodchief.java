package magic.card;

import magic.model.*;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.target.MagicWeakenTargetPicker;

public class Drana__Kalastria_Bloodchief {

	public static final MagicPermanentActivation V583 =new MagicPermanentActivation(            "Drana, Kalastria Bloodchief",
			new MagicCondition[]{MagicManaCost.X_BLACK_BLACK.getCondition()},
            new MagicActivationHints(MagicTiming.Removal),
            "Pump"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.X_BLACK_BLACK)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final int amount=payedCost.getX();
			return new MagicEvent(source,source.getController(),MagicTargetChoice.TARGET_CREATURE,
				new MagicWeakenTargetPicker(0,1),new Object[]{source,amount},this,
				"Target creature$ gets -0/-"+amount+" until end of turn and Drana, Kalastria Bloodchief gets +"+amount+"/+0 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final int amount=(Integer)data[1];
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeTurnPTAction(creature,0,-amount));
			}
			game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],amount,0));
		}
	};

}
