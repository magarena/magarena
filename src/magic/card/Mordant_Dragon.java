package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicPumpActivation;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;


public class Mordant_Dragon {
	public static final MagicPermanentActivation A = new MagicPumpActivation(MagicManaCost.ONE_RED,1,0);
	
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
			final int amount=damage.getAmount();
			return (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&damage.isCombat()) ?
				new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new MagicMayChoice(
                        "You may have " + permanent + " deal that much damage to target creature.",
                        MagicTargetChoice.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS),
                    new MagicDamageTargetPicker(amount),
                    MagicEvent.NO_DATA,
                    new MagicEventAction() {
                    @Override
                    public void executeEvent(
                            final MagicGame game,
                            final MagicEvent event,
                            final Object data[],
                            final Object[] choiceResults) {
                        if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                            event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
                                public void doAction(final MagicPermanent creature) {
                                    final MagicDamage damage=new MagicDamage(permanent.map(game),creature,amount,false);
                                    game.doAction(new MagicDealDamageAction(damage));
                                }
                            });
                        }
                    }},
				    "You may$ have " + permanent + " deal "+amount+" damage to target creature$ your opponent controls."):
				    MagicEvent.NONE;
		}
		
    };
}
