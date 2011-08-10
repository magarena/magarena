package magic.card;

import magic.model.*;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicDealDamageAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Balefire_Liege {

    public static final MagicTrigger V6812 =new MagicTrigger(MagicTriggerType.WhenSpellIsPlayed,"Balefire Liege") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			final MagicCard card=((MagicCardOnStack)data).getCard();
			if (card.getOwner()==player&&MagicColor.Red.hasColor(card.getColorFlags())) {
				return new MagicEvent(permanent,player,MagicTargetChoice.NEG_TARGET_PLAYER,
					new Object[]{permanent},this,"Balefire Liege deals 3 damage to target player$.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=event.getTarget(game,choiceResults,0);
			if (player!=null) {
				final MagicDamage damage=new MagicDamage((MagicPermanent)data[0],player,3,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}		
    };
    
    public static final MagicTrigger V6837 =new MagicTrigger(MagicTriggerType.WhenSpellIsPlayed,"Balefire Liege") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicCard card=((MagicCardOnStack)data).getCard();
			if (card.getOwner()==player&&MagicColor.White.hasColor(card.getColorFlags())) {
				return new MagicEvent(permanent,player,new Object[]{player},this,"You gain 3 life.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {			
			
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],3));
		}
    };

}
