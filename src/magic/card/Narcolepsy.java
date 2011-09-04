package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicTapAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicNoCombatTargetPicker;
import magic.model.trigger.MagicAtUpkeepTrigger;


public class Narcolepsy {
	public static final MagicSpellCardEvent S = new MagicPlayAuraEvent(
			MagicTargetChoice.NEG_TARGET_CREATURE,
            new MagicNoCombatTargetPicker(true,true,true));

    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
			final MagicPermanent enchantedCreature=permanent.getEnchantedCreature();
            return (enchantedCreature!=MagicPermanent.NONE&&!enchantedCreature.isTapped()) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new Object[]{permanent},
                        this,
                        "If "+enchantedCreature+" is untapped, tap it.") :
                null;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPermanent permanent=(MagicPermanent)data[0];
			final MagicPermanent enchantedCreature=permanent.getEnchantedCreature();
			if (enchantedCreature!=MagicPermanent.NONE&&!enchantedCreature.isTapped()) {
				game.doAction(new MagicTapAction(enchantedCreature,true));
			}
		}		
    };
}
