package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.action.MagicExileUntilThisLeavesPlayAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicExileTargetPicker;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Leonin_Relic_Warder {
	public static final MagicWhenComesIntoPlayTrigger T1 = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
			final MagicTargetFilter targetFilter = new MagicTargetFilter.MagicOtherPermanentTargetFilter(
	                MagicTargetFilter.TARGET_ARTIFACT_OR_ENCHANTMENT,permanent);
			final MagicTargetChoice targetChoice = new MagicTargetChoice(
	                targetFilter,true,MagicTargetHint.Negative,"another artifact or enchantment to exile");
	        final MagicChoice mayChoice = new MagicMayChoice(player + " may exile another artifact or enchantment.",targetChoice);
			return new MagicEvent(
                    permanent,
                    player,
                    mayChoice,
                    MagicExileTargetPicker.create(),
                    new Object[]{permanent},
                    this,
                    player + " may$ exile another artifact or enchantment$.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPermanent permanent = (MagicPermanent)data[0];
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
					public void doAction(final MagicPermanent target) {
						game.doAction(new MagicExileUntilThisLeavesPlayAction(permanent,target));
					}
				});
			}
		}
    };
}
