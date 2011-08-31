package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicDrawAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Sword_of_Fire_and_Ice {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenDamageIsDealt) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicDamage damage=(MagicDamage)data;
            final MagicPlayer player=permanent.getController();
			return (damage.getSource()==permanent.getEquippedCreature()&&damage.getTarget().isPlayer()&&damage.isCombat()) ?
                new MagicEvent(
                        permanent,
                        player,
                        MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                        new MagicDamageTargetPicker(2),
                        new Object[]{permanent,player},
                        this,
                        permanent + " deals 2 damage to target creature or player$ and you draw a card."):
                null;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            event.processTarget(game,choiceResults,0,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage=new MagicDamage((MagicSource)data[0],target,2,false);
                    game.doAction(new MagicDealDamageAction(damage));
                }
			});
			game.doAction(new MagicDrawAction((MagicPlayer)data[1],1));
		}
    };
}
