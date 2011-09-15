package magic.card;

import magic.model.MagicAbility;
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
            return (otherPermanent.isCreature()&&otherPermanent.getController()==player) ?
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
			MagicPermanent creature = (MagicPermanent)data[1];
			if (!creature.hasAbility(game, MagicAbility.Shroud)) {
				game.doAction(new MagicAttachEquipmentAction((MagicPermanent)data[0],creature));
			}
		}
    };
}
