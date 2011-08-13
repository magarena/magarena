package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Perimeter_Captain {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenBlocks) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPermanent creature=(MagicPermanent)data;
			final MagicPlayer player=permanent.getController();
            return (creature.getController() == player &&
                    creature.hasAbility(game,MagicAbility.Defender)) ?
				new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player},
                        this,
                        "You gain 2 life.") :
                null;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],2));
		}
    };
}
