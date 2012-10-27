package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;

public class Essence_Sliver {
    	
	public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
				
		@Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final int amount=damage.getDealtAmount();
            final MagicSource source=damage.getSource();
            final MagicPermanent sourcePermanent=(MagicPermanent)source;
            final MagicPlayer player=sourcePermanent.getController();
            return (sourcePermanent.hasSubType(MagicSubType.Sliver)) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player,amount},
                        this,
                        player + " gains " + amount + " life.") :
                MagicEvent.NONE;
	}
	
	
	 @Override
     public void executeEvent(
             final MagicGame game,
             final MagicEvent event,
             final Object data[],
             final Object[] choiceResults) {
         game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],(Integer)data[1]));
     }
	};
	
	
}
