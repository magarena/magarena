package magic.card;
import java.util.*;
import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.trigger.*;
import magic.model.condition.*;
import magic.model.*;
import magic.data.*;
import magic.model.variable.*;

public class Vedalken_Mastermind {

	public static final MagicPermanentActivation V2140 =new MagicPermanentActivation(            "Vedalken Mastermind",
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicManaCost.BLUE.getCondition()},
            new MagicActivationHints(MagicTiming.Removal),
            "Return"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.BLUE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.TARGET_PERMANENT_YOU_CONTROL,
                    MagicBounceTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    "Return target permanent you control$ to its owner's hand.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.OwnersHand));
			}
		}
	};
	
}
