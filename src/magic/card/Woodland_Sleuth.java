package magic.card;

import java.util.List;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Woodland_Sleuth {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
			return (game.getCreatureDiedThisTurn()) ?
				new MagicEvent(
                    permanent,
                    player,
                    new Object[]{player,permanent},
                    this,
                    "Return a creature card at random from your graveyard to your hand.") :
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPlayer player = (MagicPlayer)data[0];
			final List<MagicTarget> targets =
					game.filterTargets(player,MagicTargetFilter.TARGET_CREATURE_CARD_FROM_GRAVEYARD);
			if (targets.size() > 0) {
				final MagicPermanent permanent = (MagicPermanent)data[1];
				final magic.MersenneTwisterFast rng = 
						new magic.MersenneTwisterFast(permanent.getId() + player.getId());
				final int index = rng.nextInt(targets.size());
				final MagicCard card = (MagicCard)targets.get(index);
				game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
				game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
			}
		}
    };
}
