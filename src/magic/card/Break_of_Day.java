package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Break_of_Day {
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    new Object[]{cardOnStack,player},
                    this,
                    "Creatures you control get +1/+1 until end of turn.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPlayer player = (MagicPlayer)data[1];
			final Collection<MagicTarget> targets = game.filterTargets(
					player,
					MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			final boolean fatefulHour = player.getLife() <= 5;
			for (final MagicTarget target : targets) {
				final MagicPermanent creature = (MagicPermanent)target;
				game.doAction(new MagicChangeTurnPTAction(creature,1,1));
				if (fatefulHour) {
					game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Indestructible));
				}
			}
		}
	};
}
