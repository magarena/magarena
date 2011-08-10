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

public class Ravenous_Baloth {

	public static final MagicPermanentActivation V1623 =new MagicPermanentActivation(			"Ravenous Baloth",
            new MagicCondition[]{MagicCondition.CONTROL_BEAST_CONDITION},
            new MagicActivationHints(MagicTiming.Pump,true),
            "Life+4") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificePermanentEvent(source,source.getController(),MagicTargetChoice.SACRIFICE_BEAST)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(source,player,new Object[]{player},this,"You gain 4 life.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],4));
		}
	};
	
}
