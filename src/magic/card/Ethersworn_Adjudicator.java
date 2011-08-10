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

public class Ethersworn_Adjudicator {

	public static final MagicPermanentActivation V755 =new MagicPermanentActivation(            "Ethersworn Adjudicator",
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicManaCost.ONE_WHITE_BLACK.getCondition()},
            new MagicActivationHints(MagicTiming.Removal),
            "Destroy") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.ONE_WHITE_BLACK)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(source,source.getController(),MagicTargetChoice.NEG_TARGET_CREATURE_OR_ENCHANTMENT,new MagicDestroyTargetPicker(false),
				MagicEvent.NO_DATA,this,"Destroy target creature or enchantment$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPermanent permanent=event.getTarget(game,choiceResults,0);
			if (permanent!=null) {
				game.doAction(new MagicDestroyAction(permanent));
			}
		}
	};

	public static final MagicPermanentActivation V780 =new MagicPermanentActivation(            "Ethersworn Adjudicator",
			new MagicCondition[]{
                MagicCondition.TAPPED_CONDITION,
                MagicManaCost.TWO_BLUE.getCondition(),
                new MagicSingleActivationCondition()},
			new MagicActivationHints(MagicTiming.Tapping),
            "Untap") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.TWO_BLUE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(source,source.getController(),new Object[]{source},this,"Untap Ethersworn Adjudicator.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			game.doAction(new MagicUntapAction((MagicPermanent)data[0]));
		}
	};

}
