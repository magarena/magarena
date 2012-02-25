package magic.card;

import java.util.List;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

public class Zombie_Apocalypse {
	public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(
				final MagicCardOnStack cardOnStack,
				final MagicPayedCost payedCost) {
			final MagicPlayer player = cardOnStack.getController();
			return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    new Object[]{cardOnStack,player},
                    this,
                    "Return all Zombie creature cards from your graveyard " +
                    "to the battlefield tapped, then destroy all Humans.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPlayer player = (MagicPlayer)data[1];
			final List<MagicTarget> zombies =
					game.filterTargets(player,MagicTargetFilter.TARGET_ZOMBIE_CARD_FROM_GRAVEYARD);
			for (final MagicTarget target : zombies) {
				game.doAction(new MagicReanimateAction(
						player,
						(MagicCard)target,
						MagicPlayCardAction.TAPPED));
			}
			final List<MagicTarget> humans =
					game.filterTargets(player,MagicTargetFilter.TARGET_HUMAN);
			for (final MagicTarget target : humans) {
				game.doAction(new MagicDestroyAction((MagicPermanent)target));
			}
		}
	};
}
