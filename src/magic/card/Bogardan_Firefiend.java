package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicWhenPutIntoGraveyardTrigger;

public class Bogardan_Firefiend {
    public static final MagicWhenPutIntoGraveyardTrigger T = new MagicWhenPutIntoGraveyardTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicGraveyardTriggerData triggerData) {
			return (MagicLocationType.Play == triggerData.fromLocation) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        MagicTargetChoice.TARGET_CREATURE,
                        new MagicDamageTargetPicker(2),
                        new Object[]{permanent},
                        this,
                        permanent + " deals 2 damage to target creature$."):
                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final MagicDamage damage = new MagicDamage((MagicPermanent)data[0],creature,2,false);
                    game.doAction(new MagicDealDamageAction(damage));
                }
			});
		}
    };
}
