package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayTokenAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicPumpTargetPicker;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Elephant_Guide {
	public static final MagicSpellCardEvent S = new MagicPlayAuraEvent(
			MagicTargetChoice.POS_TARGET_CREATURE,
            MagicPumpTargetPicker.getInstance());

    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenOtherPutIntoGraveyardFromPlay) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
			if (permanent.getEnchantedCreature()==data) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player},
                        this,
                        "Put a 3/3 green Elephant creature token onto the battlefield.");
			}			
			return null;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.ELEPHANT_TOKEN_CARD));
		}
    };
}
