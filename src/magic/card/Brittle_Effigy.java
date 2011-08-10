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

public class Brittle_Effigy {

	public static final MagicPermanentActivation V2574 =new MagicPermanentActivation(            "Brittle Effigy",
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicManaCost.FOUR.getCondition()},
            new MagicActivationHints(MagicTiming.Removal),
            "Exile") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
				new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.FOUR),
				new MagicExileEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    MagicExileTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    "Exile target creature$.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.Exile));
			}
		}
	};

	
}
