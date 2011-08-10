package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Eldrazi_Monument {

    public static final MagicTrigger V10300 =new MagicTrigger(MagicTriggerType.AtUpkeep,"Eldrazi Monument") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(permanent,player,new Object[]{permanent,player},this,"Sacrifice a creature. If you can't, sacrifice Eldrazi Monument.");
			}
			return null;
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			final MagicPermanent permanent=(MagicPermanent)data[0];
			final MagicPlayer player=(MagicPlayer)data[1];
			if (player.controlsPermanentWithType(MagicType.Creature)) {
				game.addEvent(new MagicSacrificePermanentEvent(permanent,player,MagicTargetChoice.SACRIFICE_CREATURE));
			} else {
				game.doAction(new MagicSacrificeAction(permanent));				
			}			
		}
    };
    
}
