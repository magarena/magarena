package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Smallpox {
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(
				final MagicCardOnStack cardOnStack,
				final MagicPayedCost payedCost) {
			final MagicPlayer player = cardOnStack.getController();
			return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
				    new Object[]{cardOnStack},
				    this,
				    "Each player loses 1 life, discards a card, " +
				    "sacrifices a creature, then sacrifices a land.");
		}

		@Override
		public void executeEvent(
				final MagicGame game,
				final MagicEvent event,
				final Object[] data,
				final Object[] choiceResults) {
			final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			for (final MagicPlayer player : game.getPlayers()) {
				game.doAction(new MagicChangeLifeAction(player,-1));
				game.addEvent(new MagicDiscardEvent(
						cardOnStack.getCard(),
						player,
						1,
						false));
				if (player.controlsPermanentWithType(MagicType.Creature,game)) {
    				game.addEvent(new MagicSacrificePermanentEvent(
                        cardOnStack.getCard(),
                        player,
                        MagicTargetChoice.SACRIFICE_CREATURE));
                }
				if (player.controlsPermanentWithType(MagicType.Land,game)) {
    				game.addEvent(new MagicSacrificePermanentEvent(
                        cardOnStack.getCard(),
                        player,
                        MagicTargetChoice.SACRIFICE_LAND));
                }
			}
		}
	};

}
