package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;


public class Spitemare {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final int amount=damage.getDealtAmount();
			return (damage.getTarget()==permanent) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        MagicTargetChoice.TARGET_CREATURE_OR_PLAYER,
                        new MagicDamageTargetPicker(amount),
                        new Object[]{permanent,amount},
                        this,
                        permanent + " deals "+amount+" damage to target creature or player$."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            event.processTarget(game,choiceResults,0,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage=new MagicDamage((MagicSource)data[0],target,(Integer)data[1],false);
                    game.doAction(new MagicDealDamageAction(damage));
                }
			});
		}
    };
}
