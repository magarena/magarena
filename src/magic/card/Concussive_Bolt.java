package magic.card;

import java.util.Collection;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayerAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

public class Concussive_Bolt {
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player = cardOnStack.getController();
            final MagicCard card = cardOnStack.getCard();
			return new MagicEvent(
                    card,
                    player,
                    MagicTargetChoice.NEG_TARGET_PLAYER,
                    new MagicDamageTargetPicker(4),
                    new Object[]{cardOnStack,player},
                    this,
                    card + " deals 4 damage to target player$.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));			
			event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
                public void doAction(final MagicPlayer targetPlayer) {
                    final MagicDamage damage = new MagicDamage(cardOnStack.getCard(),targetPlayer,4,false);
                    game.doAction(new MagicDealDamageAction(damage));
                    final MagicPlayer player = (MagicPlayer)data[1];
                    if (player.getNrOfPermanentsWithType(MagicType.Artifact,game) >= 3) {
                    	final Collection<MagicTarget> targets =
                    			game.filterTargets(targetPlayer,MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
                    	for (final MagicTarget target : targets) {
                    		game.doAction(new MagicSetAbilityAction(
                    				(MagicPermanent)target,
                    				MagicAbility.CannotBlock));
                    	}
                    }
                }
			});
		}
	};
}
