package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;


public class Mask_of_Memory {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPlayer player=permanent.getController();
			return (permanent.getEquippedCreature()==damage.getSource()&&damage.getTarget().isPlayer()&&damage.isCombat()) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicSimpleMayChoice(
                            "You may draw two cards.",
                            MagicSimpleMayChoice.DRAW_CARDS,
                            2,
                            MagicSimpleMayChoice.DEFAULT_NONE),
                        new Object[]{permanent,player},
                        this,
                        "You may$ draw two cards. If you do, discard a card."):
                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			if (MagicChoice.isYesChoice(choiceResults[0])) {
				final MagicPlayer player=(MagicPlayer)data[1];
				game.doAction(new MagicDrawAction(player,2));
				game.addEvent(new MagicDiscardEvent((MagicPermanent)data[0],player,1,false));
			}
		}
    };
}
