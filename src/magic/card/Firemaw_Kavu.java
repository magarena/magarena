package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.trigger.MagicEchoTrigger;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;

public class Firemaw_Kavu {
    public static final MagicWhenLeavesPlayTrigger T3 = new MagicWhenLeavesPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent data) {
			final MagicPlayer player = permanent.getController();
			return (permanent == data) ?
					new MagicEvent(
							permanent,
		                    player,
		                    MagicTargetChoice.TARGET_CREATURE,
		                    new MagicDamageTargetPicker(4),
		                    new Object[]{permanent},
		                    this,
		                    permanent + " deals 4 damage to target creature$.") :
					MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
				public void doAction(final MagicPermanent creature) {
                    final MagicDamage damage = new MagicDamage((MagicPermanent)data[0],creature,4,false);
                    game.doAction(new MagicDealDamageAction(damage));
                }
			});
		}
    };
}
