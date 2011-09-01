package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Suture_Priest {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenOtherComesIntoPlay) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            final MagicPlayer player=permanent.getController();
            final MagicPlayer controller=otherPermanent.getController();
            final boolean same=controller==player;
			return (otherPermanent!=permanent&&otherPermanent.isCreature()) ?
                new MagicEvent(
                    permanent,
                    player,
                    new Object[]{controller,same?1:-1},
                    this,
                    controller + (same ? " gains 1 life." : " loses 1 life.")):
                null;
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
