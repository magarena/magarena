package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayTokenAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicCopyTargetPicker;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Followed_Footsteps {
	public static final MagicSpellCardEvent S = new MagicPlayAuraEvent(
			MagicTargetChoice.TARGET_CREATURE,
            MagicCopyTargetPicker.getInstance());

    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.AtUpkeep) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPlayer player=permanent.getController();
			final MagicPermanent enchantedCreature=permanent.getEnchantedCreature();
			if (player==data && enchantedCreature!=null) {
				return new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent,player},
                        this,
                        player + " puts a token that's a copy of enchanted creature onto the battlefield.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPermanent permanent=(MagicPermanent)data[0];
			final MagicPermanent enchantedCreature=permanent.getEnchantedCreature();
			if (enchantedCreature!=null) {
				game.doAction(new MagicPlayTokenAction((MagicPlayer)data[1],enchantedCreature.getCardDefinition()));
			}
		}		
    };
}
