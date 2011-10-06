package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicSetAbilityAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Fledgling_Griffin {
    public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent played) {
			final MagicPlayer player = permanent.getController();
			return (player == played.getController() && played.isLand()) ?
				new MagicEvent(
                    permanent,
                    player,
                    new Object[]{permanent},
                    this,
                    permanent + " gains flying until end of turn."):
                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicSetAbilityAction((MagicPermanent)data[0],MagicAbility.Flying));
		}		
    };
}
