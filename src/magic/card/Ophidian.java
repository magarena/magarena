package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicDrawAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicIfDamageWouldBeDealtTrigger;

public class Ophidian {
	public static final MagicIfDamageWouldBeDealtTrigger T = new MagicIfDamageWouldBeDealtTrigger(1) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
			if (damage.getSource() == permanent &&
				damage.isCombat() &&
				damage.getTarget().isPlayer() &&
				!permanent.isBlocked()) {
				final MagicPlayer player = permanent.getController();
				final int amount = damage.getAmount();
				damage.setAmount(0);
				return new MagicEvent(
						permanent,
						player,
						new MagicSimpleMayChoice(
								player + " may draw a card.",
								MagicSimpleMayChoice.DRAW_CARDS,
								1,
								MagicSimpleMayChoice.DEFAULT_NONE),
								new Object[]{player,permanent,damage.getTarget(),amount},
								this,
								player + " may draw a card.");
            }
            return MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				game.doAction(new MagicDrawAction((MagicPlayer)data[0],1));
			} else {
				final MagicDamage damage = new MagicDamage(
						(MagicPermanent)data[1],
						(MagicTarget)data[2],
						(Integer)data[3],
						false);
                game.doAction(new MagicDealDamageAction(damage));
			}	
		}
    };
}
