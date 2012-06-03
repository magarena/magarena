package magic.card;

import java.util.Collection;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicPowerTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

public class Alpha_Brawl {
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player = cardOnStack.getController();
			return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    MagicTargetChoice.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                    MagicPowerTargetPicker.getInstance(),
				    new Object[]{cardOnStack,player},
				    this,
				    "Target creature$ an opponent controls deals damage equal to " +
				    "its power to each other creature that player controls, then " +
				    "each of those creatures deals damage equal to its power to that creature.");
		}

		@Override
		public void executeEvent(
				final MagicGame game,
				final MagicEvent event,
				final Object[] data,
				final Object[] choiceResults) {
			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
            	final MagicPlayer player = (MagicPlayer)data[1];
                public void doAction(final MagicPermanent permanent) {
                	final Collection<MagicTarget> creatures = 
                			game.filterTargets(game.getOpponent(player),MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
    				for (final MagicTarget creature : creatures) {
    					final MagicDamage damage = new MagicDamage(
    							permanent,
    							creature,
    							permanent.getPower(),
    							false);
    					game.doAction(new MagicDealDamageAction(damage));
    				}
    				for (final MagicTarget creature : creatures) {
    					final MagicPermanent source = (MagicPermanent)creature;
    					final MagicDamage damage = new MagicDamage(
    							source,
    							permanent,
    							source.getPower(),
    							false);
    					game.doAction(new MagicDealDamageAction(damage));
    				}
                }
			});
		}
	};

}
