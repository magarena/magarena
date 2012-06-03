package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicPlayerAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenOtherPutIntoGraveyardFromPlayTrigger;

public class Blood_Artist {
    public static final MagicWhenOtherPutIntoGraveyardFromPlayTrigger T = new MagicWhenOtherPutIntoGraveyardFromPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(
				final MagicGame game,
				final MagicPermanent permanent,
				final MagicPermanent otherPermanent) {
			final MagicPlayer controller = permanent.getController();
			return (otherPermanent.isCreature()) ?
				new MagicEvent(
                    permanent,
                    controller,
                    MagicTargetChoice.NEG_TARGET_PLAYER,
                    new Object[]{controller},
                    this,
                    "Target player$ loses 1 life and " +
                    controller + " gains 1 life."):
                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    game.doAction(new MagicChangeLifeAction(player,-1));
                }
			});
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],1));
		}
    };
}
