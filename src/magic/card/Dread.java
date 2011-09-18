package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDestroyAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicFromGraveyardToLibraryTrigger;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;


public class Dread {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
			final MagicPlayer player=permanent.getController();
			return (damage.getTarget()==player && 
                    damage.getSource().isCreature(game)) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{damage.getSource()},
                        this,
                        "Destroy "+damage.getSource()+"."):
                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicDestroyAction((MagicPermanent)data[0]));
		}
    };
    
    public static final MagicFromGraveyardToLibraryTrigger T2 = new MagicFromGraveyardToLibraryTrigger();
}
