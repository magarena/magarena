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

public class Omnibian {

	public static final MagicPermanentActivation V1415 =new MagicPermanentActivation(			"Omnibian",
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Removal),
            "Frog") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.TARGET_CREATURE,
                    new MagicBecomeTargetPicker(3,3,false),
                    MagicEvent.NO_DATA,this,"Target creature$ becomes a 3/3 Frog until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicBecomesCreatureAction(creature,
        new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.power=3;
			pt.toughness=3;
		}

		@Override
		public EnumSet<MagicSubType> getSubTypeFlags(final MagicPermanent permanent,final EnumSet<MagicSubType> flags) {
			return EnumSet.of(MagicSubType.Frog);
		}
	}));
			}
		}
	};
	
}
