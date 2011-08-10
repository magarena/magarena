package magic.card;

import magic.model.*;
import magic.model.action.MagicChangeCountersAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.*;
import magic.model.target.MagicWeakenTargetPicker;

public class Fume_Spitter {

	public static final MagicPermanentActivation V894 =new MagicPermanentActivation(            "Fume Spitter",
            null,
            new MagicActivationHints(MagicTiming.Removal),
            "-1/-1") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificeEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(source,source.getController(),MagicTargetChoice.NEG_TARGET_CREATURE,new MagicWeakenTargetPicker(1,1),
				MagicEvent.NO_DATA,this,"Put a -1/-1 counter on target creature$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicPermanent creature=(MagicPermanent)event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeCountersAction(creature,MagicCounterType.MinusOne,1,true));
			}
		}
	};
	
}
