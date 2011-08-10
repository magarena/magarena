package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicMillLibraryAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Nemesis_of_Reason {

    public static final MagicTrigger V8272 =new MagicTrigger(MagicTriggerType.WhenAttacks,"Nemesis of Reason") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent==data) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{game.getOpponent(player)},this,
					"Defending player puts the top ten cards of his or her library into his or her graveyard.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicMillLibraryAction((MagicPlayer)data[0],10));
		}
    };

}
