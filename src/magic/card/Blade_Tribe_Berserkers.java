package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.MagicType;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Blade_Tribe_Berserkers {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
			if (player.getNrOfPermanentsWithType(MagicType.Artifact,game) >= 3) {
				game.doAction(new MagicChangeTurnPTAction(permanent,3,3));
                game.doAction(new MagicSetAbilityAction(permanent,MagicAbility.Haste));
			}
			return MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
		}
    };
}
