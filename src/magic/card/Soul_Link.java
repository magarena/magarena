package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicNoCombatTargetPicker;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Soul_Link {

	public static final MagicSpellCardEvent V6526 =new MagicPlayAuraEvent("Soul Link",
			MagicTargetChoice.TARGET_CREATURE,new MagicNoCombatTargetPicker(true,true,true));	
    public static final MagicTrigger V10703 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Soul Link") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent.getEnchantedCreature()) {
				final MagicPlayer player=permanent.getController();
				final int amount=damage.getDealtAmount();
				return new MagicEvent(permanent,player,new Object[]{player,amount},this,"You gain "+amount+" life.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],(Integer)data[1]));
		}
    };
    
    public static final MagicTrigger V10724 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Soul Link") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getTarget()==permanent.getEnchantedCreature()) {
				final MagicPlayer player=permanent.getController();
				final int amount=damage.getDealtAmount();
				return new MagicEvent(permanent,player,new Object[]{player,amount},this,"You gain "+amount+" life.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],(Integer)data[1]));
		}
    };
    
}
