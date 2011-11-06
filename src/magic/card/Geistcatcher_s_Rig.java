package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Geistcatcher_s_Rig {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
			return new MagicEvent(
                    permanent,
                    player,
                    new MagicMayChoice(
                    	player + " may have " + permanent + " deal 4 damage " +
                    	"to target creature with flying.",
                        MagicTargetChoice.NEG_TARGET_CREATURE_WITH_FLYING),
                    new MagicDamageTargetPicker(4),
                    new Object[]{permanent},
                    this,
                    player + " may$ have " + permanent + " deal 4 damage " +
                    "to target creature with flying$.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
					public void doAction(final MagicPermanent creature) {
						final MagicDamage damage = new MagicDamage((MagicPermanent)data[0],creature,4,false);
						game.doAction(new MagicDealDamageAction(damage));
					}
				});
			}
		}
    };
}
