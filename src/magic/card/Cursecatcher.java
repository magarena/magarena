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

public class Cursecatcher {

	public static final MagicPermanentActivation V492 =new MagicPermanentActivation(			"Cursecatcher",
            null,
            new MagicActivationHints(MagicTiming.Counter),
            "Counter"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {

			return new MagicEvent[]{new MagicSacrificeEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {

			return new MagicEvent(source,source.getController(),MagicTargetChoice.NEG_TARGET_INSTANT_OR_SORCERY_SPELL,
				new Object[]{source},this,"Counter target instant or sorcery spell$ unless its controller pays {1}.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack targetSpell=event.getTarget(game,choiceResults,0);
			if (targetSpell!=null) {
				game.addEvent(new MagicCounterUnlessEvent((MagicSource)data[0],targetSpell,MagicManaCost.ONE)); 
			}
		}
	};
	
}
