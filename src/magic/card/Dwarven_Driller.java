package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTapEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicDestroyTargetPicker;

public class Dwarven_Driller {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Removal),
            "Destroy") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(
				final MagicPermanent source,
				final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_LAND,
                    new MagicDestroyTargetPicker(false),
                    new Object[]{source.getController()},
                    this,
                    "Destroy target land$ unless its controller has " +
                    source + " deal 2 damage to him or her.");
		}

		@Override
		public void executeEvent(
				final MagicGame game,
				final MagicEvent event,
				final Object[] data,
				final Object[] choiceResults) {
			event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                	final MagicPlayer controller = permanent.getController();
                	game.addEvent(new MagicEvent(
                    		event.getSource(),
                    		controller,
            				new MagicMayChoice(
            						controller + " may have " +
            						event.getSource() + " deal 2 damage to him or her."),
                            new Object[]{controller,permanent},
                            EVENT_ACTION,
                            controller + " may$ have " +
                            event.getSource() + " deal 2 damage to him or her."));
                }
			});
		}
		private final MagicEventAction EVENT_ACTION = new MagicEventAction() {
	        @Override
	        public void executeEvent(
	                final MagicGame game,
	                final MagicEvent event,
	                final Object[] data,
	                final Object[] choiceResults) {
	        	if (MagicMayChoice.isYesChoice(choiceResults[0])) {
					final MagicDamage damage = new MagicDamage(
							event.getSource(),
							(MagicPlayer)data[0],
							2,
							false);
                    game.doAction(new MagicDealDamageAction(damage));
				} else {
					game.doAction(new MagicDestroyAction((MagicPermanent)data[1]));
				}
	        }
	    };
	};
}
