package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicAttachEquipmentAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Ronin_Warclub {
    public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
			final MagicPlayer player=permanent.getController();
            return (otherPermanent.isCreature(game) && 
                    otherPermanent.getController()==player) ?
				new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent,otherPermanent},
                        this,
                         "Attach " + permanent + " to " + otherPermanent + ".") :
                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicAttachEquipmentAction((MagicPermanent)data[0],(MagicPermanent)data[1]));
		}
    };
}
