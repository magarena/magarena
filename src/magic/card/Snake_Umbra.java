package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicPumpTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Snake_Umbra {

	public static final MagicSpellCardEvent V6524 =new MagicPlayAuraEvent("Snake Umbra",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicPumpTargetPicker.getInstance());	
    public static final MagicTrigger V10678 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Snake Umbra") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			final MagicDamage damage=(MagicDamage)data;
			final MagicTarget target=damage.getTarget();
			if (damage.getSource()==permanent.getEnchantedCreature()&&target.isPlayer()&&target!=player) {
				return new MagicEvent(permanent,player,
	new MagicSimpleMayChoice("You may draw a card.",MagicSimpleMayChoice.DRAW_CARDS,1),
    new Object[]{player},this,"You may$ draw a card.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			if (MagicChoice.isYesChoice(choiceResults[0])) {
				game.doAction(new MagicDrawAction((MagicPlayer)data[0],1));
			}
		}
    };
    
}
