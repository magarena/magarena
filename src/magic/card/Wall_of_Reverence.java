package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicPowerTargetPicker;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Wall_of_Reverence {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.AtEndOfTurn) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(
                        permanent,
                        player,
                        MagicTargetChoice.TARGET_CREATURE_YOU_CONTROL,
                        MagicPowerTargetPicker.getInstance(),
                        new Object[]{player},
                        this,
                        "You gain life equal to the power of target creature$ you control.");
			}
			return null;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],creature.getPower(game)));
			}
		}
    };
}
