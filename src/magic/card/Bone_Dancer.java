package magic.card;

import java.util.List;

import magic.model.MagicCard;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicIfDamageWouldBeDealtTrigger;

public class Bone_Dancer {
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
						new MagicMayChoice(
	                            player + " may put the top creature card of " +
	                            damage.getTarget() + "'s graveyard onto the " +
	                            "battlefield under his or her control."),
						new Object[]{player,permanent,damage.getTarget(),amount},
						this,
						player + " may put the top creature card of " +
	                    damage.getTarget() + "'s graveyard onto the " +
	                    "battlefield under his or her control.");
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
				final MagicPlayer opponent = (MagicPlayer)data[2];
				final List<MagicTarget> targets =
		                game.filterTargets(opponent,MagicTargetFilter.TARGET_CREATURE_CARD_FROM_GRAVEYARD);
				if (targets.size() > 0) {
					final MagicCard card = (MagicCard)targets.get(targets.size()-1);
					game.doAction(new MagicReanimateAction(
							(MagicPlayer)data[0],
							card,
							MagicPlayCardAction.NONE));
				}
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
