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

public class Arcanis_the_Omnipotent {

	public static final MagicPermanentActivation V139 = new MagicPermanentActivation("Arcanis the Omnipotent",
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Token),
            "Draw"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(source,player,new Object[]{player},this,"You draw three cards.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicDrawAction((MagicPlayer)data[0],3));
		}
	};
	
	public static final MagicPermanentActivation V166 =new MagicPermanentActivation(            "Arcanis the Omnipotent",
			new MagicCondition[]{MagicManaCost.TWO_BLUE_BLUE.getCondition()},
            new MagicActivationHints(MagicTiming.Removal),
            "Return"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.TWO_BLUE_BLUE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    "Return Arcanis the Omnipotent to its owner's hand.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicRemoveFromPlayAction((MagicPermanent)data[0],MagicLocationType.OwnersHand));
		}
	};
	
}
