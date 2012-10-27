package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;
import magic.model.MagicSource;
import magic.model.MagicSubType;

public class Synapse_Sliver {
	public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
			
			
			final MagicSource source=damage.getSource();
            final MagicPermanent sourcePermanent=(MagicPermanent)source;
            final MagicPlayer player=sourcePermanent.getController();
			
			
			return (damage.getTarget() == player.getOpponent() && sourcePermanent.hasSubType(MagicSubType.Sliver)  &&
					damage.isCombat()) ?
							new MagicEvent(
									permanent,
									player,
									new MagicSimpleMayChoice(
											player + " may draw a card.",
											MagicSimpleMayChoice.DRAW_CARDS,
											1,
											MagicSimpleMayChoice.DEFAULT_NONE),
											new Object[]{player},
											this,
											player + " may$ draw a card.") :
												MagicEvent.NONE;
		}

		@Override
		public void executeEvent(
				final MagicGame game,
				final MagicEvent event,
				final Object data[],
				final Object[] choiceResults) {
			if (MagicChoice.isYesChoice(choiceResults[0])) {
				game.doAction(new MagicDrawAction((MagicPlayer)data[0],1));
			}
		}
	};
}
