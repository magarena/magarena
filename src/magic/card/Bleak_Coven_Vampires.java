package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.MagicType;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicPlayerAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Bleak_Coven_Vampires {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
			return player.getNrOfPermanentsWithType(MagicType.Artifact) >= 3 ?
				new MagicEvent(
					permanent,
					player,
					MagicTargetChoice.NEG_TARGET_PLAYER,
					new Object[]{player},
					this,
					"Target player$ loses 4 life and " +
					player + " gains 4 life.") :
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
                    game.doAction(new MagicChangeLifeAction(player,-4));
                }
			});
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],4));
		}
    };
}
