package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.trigger.MagicWhenAttacksTrigger;


public class Argentum_Armor {
    public static final MagicWhenAttacksTrigger T =new MagicWhenAttacksTrigger(1) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
			final MagicPermanent equippedCreature = permanent.getEquippedCreature();
			return (equippedCreature.isValid() &&
					equippedCreature == creature) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        MagicTargetChoice.NEG_TARGET_PERMANENT,
    				    new MagicDestroyTargetPicker(false),
                        MagicEvent.NO_DATA,
                        this,
						"Destroy target permanent$."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(new MagicDestroyAction(permanent));
                }
			});
		}
    };
}
