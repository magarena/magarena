package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicDealDamageAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicPumpActivation;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Mordant_Dragon {

	public static final MagicPermanentActivation V1409 = new MagicPumpActivation("Mordant Dragon",MagicManaCost.ONE_RED,1,0);
	
    public static final MagicTrigger V8110 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Mordant Dragon") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				final int amount=damage.getAmount();
				return new MagicEvent(permanent,permanent.getController(),
	new MagicMayChoice(
			"You may have Mordant Dragon deal that much damage to target creature.",MagicTargetChoice.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS),
    new MagicDamageTargetPicker(amount),
					new Object[]{permanent,amount},this,"You may$ have Mordant Dragon deal "+amount+" damage to target creature$ your opponent controls.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicPermanent creature=event.getTarget(game,choiceResults,1);
				if (creature!=null) {
					final MagicDamage damage=new MagicDamage((MagicPermanent)data[0],creature,(Integer)data[1],false);
					game.doAction(new MagicDealDamageAction(damage));
				}
			}
		}
    };
    
}
