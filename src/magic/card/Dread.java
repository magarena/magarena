package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDestroyAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicFromGraveyardToLibraryTrigger;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Dread {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenDamageIsDealt) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPlayer player=permanent.getController();
			final MagicDamage damage=(MagicDamage)data;
            final MagicPermanent source=(MagicPermanent)damage.getSource();
			return (damage.getTarget()==player&&damage.getSource().isPermanent()&&source.isCreature()) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{source},
                        this,
                        "Destroy "+source.getName()+"."):
                null;
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
    
    public static final MagicTrigger T2 = new MagicFromGraveyardToLibraryTrigger();
}
