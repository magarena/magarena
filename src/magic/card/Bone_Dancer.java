package magic.card;

import java.util.List;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenAttacksUnblockedTrigger;

public class Bone_Dancer {
	public static final MagicWhenAttacksUnblockedTrigger T = new MagicWhenAttacksUnblockedTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            if (creature == permanent) {
            	final MagicPlayer player = permanent.getController();
            	final MagicPlayer opponent = game.getOpponent(player);
            	return new MagicEvent(
						permanent,
						player,
						new MagicMayChoice(
	                            player + " may put the top creature card of " +
	                            opponent + "'s graveyard onto the " +
	                            "battlefield under his or her control."),
						new Object[]{player,opponent,permanent},
						this,
						player + " may$ put the top creature card of " +
						opponent + "'s graveyard onto the " +
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
				final MagicPlayer opponent = (MagicPlayer)data[1];
				final List<MagicTarget> targets =
		                game.filterTargets(opponent,MagicTargetFilter.TARGET_CREATURE_CARD_FROM_GRAVEYARD);
				if (targets.size() > 0) {
					final MagicCard card = (MagicCard)targets.get(targets.size()-1);
					game.doAction(new MagicReanimateAction(
							(MagicPlayer)data[0],
							card,
							MagicPlayCardAction.NONE));
				}
				game.doAction(new MagicChangeStateAction(
						(MagicPermanent)data[2],
						MagicPermanentState.NoCombatDamage,true));
			}
		}
    };
}
