package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Karmic_Guide {
	public static final MagicWhenComesIntoPlayTrigger T2 = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(
				final MagicGame game,
				final MagicPermanent permanent,
				final MagicPlayer player) {
			return new MagicEvent(
                    permanent,
                    player,
	                MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                    new MagicGraveyardTargetPicker(true),
                    new Object[]{player},
                    this,
                    "Return target creature card$ from " +
                    "your graveyard to the battlefield.");
		}
		
		@Override
		public void executeEvent(
				final MagicGame game,
				final MagicEvent event,
				final Object data[],
				final Object[] choiceResults) {
			event.processTargetCard(game,choiceResults,0,new MagicCardAction() {
                public void doAction(final MagicCard targetCard) {
					game.doAction(new MagicReanimateAction(
							(MagicPlayer)data[0],
							targetCard,
							MagicPlayCardAction.NONE));
                }
			});
		}
    };
}
