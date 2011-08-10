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

public class Kiki_Jiki__Mirror_Breaker {

	public static final MagicPermanentActivation V1230 =new MagicPermanentActivation(			"Kiki-Jiki, Mirror Breaker",
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Token),
            "Copy") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(source,player,MagicTargetChoice.TARGET_NON_LEGENDARY_CREATURE_YOU_CONTROL,
				MagicCopyTargetPicker.getInstance(),new Object[]{player},this,
				"Put a token that's a copy of target nonlegendary creature$ you control onto the battlefield. "+
				"That token has haste. Sacrifice it at end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				final MagicPlayer player=(MagicPlayer)data[0];
				final MagicCard card=MagicCard.createTokenCard(creature.getCardDefinition(),player);
				game.doAction(new MagicPlayCardAction(card,player,MagicPlayCardAction.HASTE_SACRIFICE_AT_END_OF_TURN));
			}
		}
	};
	
}
