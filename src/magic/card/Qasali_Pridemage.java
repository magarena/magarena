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

public class Qasali_Pridemage {

	public static final MagicPermanentActivation V1507 =new MagicPermanentActivation(			"Qasali Pridemage",
            new MagicCondition[]{MagicManaCost.ONE.getCondition()},
            new MagicActivationHints(MagicTiming.Removal),
            "Destory") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostSacrificeEvent(source,source.getController(),MagicManaCost.ONE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_ARTIFACT_OR_ENCHANTMENT,
                    new MagicDestroyTargetPicker(false),
                    MagicEvent.NO_DATA,
                    this,
                    "Destroy target artifact or enchantment$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPermanent permanent=event.getTarget(game,choiceResults,0);
			if (permanent!=null) {
				game.doAction(new MagicDestroyAction(permanent));
			}
		}
	};
	
}
