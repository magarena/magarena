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

public class Venser__Shaper_Savant {

    public static final MagicTrigger V9297 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Venser, Shaper Savant") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.NEG_TARGET_SPELL_OR_PERMANENT,MagicBounceTargetPicker.getInstance(),
				MagicEvent.NO_DATA,this,"Return target spell or permanent$ to its owner's hand.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				if (target.isPermanent()) {
					game.doAction(new MagicRemoveFromPlayAction((MagicPermanent)target,MagicLocationType.OwnersHand));
				} else {
					final MagicCardOnStack cardOnStack=(MagicCardOnStack)target;
					game.doAction(new MagicRemoveItemFromStackAction(cardOnStack));
					game.doAction(new MagicMoveCardAction(cardOnStack.getCard(),MagicLocationType.Stack,MagicLocationType.OwnersHand));
				}
			}
		}
    };

}
