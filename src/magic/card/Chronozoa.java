package magic.card;

import magic.model.MagicCard;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayCardAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicWhenPutIntoGraveyardTrigger;

public class Chronozoa {
    public static final MagicWhenPutIntoGraveyardTrigger T3 = new MagicWhenPutIntoGraveyardTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicGraveyardTriggerData triggerData) {
			final MagicPlayer player = permanent.getController();
			return (MagicLocationType.Play == triggerData.fromLocation &&
					permanent.getCounters(MagicCounterType.Charge) == 0) ?
				new MagicEvent(
                    permanent,
                    player,
                    new Object[]{player,permanent},
                    this,
                    player + " puts two tokens that are copies of " + permanent + " onto the battlefield.") :
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPlayer player = (MagicPlayer)data[0];
			final MagicPermanent permanent = (MagicPermanent)data[1];
			final MagicCard card = MagicCard.createTokenCard(permanent.getCardDefinition(),player);
			game.doAction(new MagicPlayCardAction(card,player,MagicPlayCardAction.NONE));
			game.doAction(new MagicPlayCardAction(card,player,MagicPlayCardAction.NONE));
		}
    };
}
