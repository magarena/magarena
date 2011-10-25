package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MagicExileUntilThisLeavesPlayAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicExileTargetPicker;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;

public class Leonin_Relic_Warder {
	public static final MagicWhenComesIntoPlayTrigger T1 = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
			final MagicTargetFilter targetFilter = new MagicTargetFilter.MagicOtherPermanentTargetFilter(
	                MagicTargetFilter.TARGET_ARTIFACT_OR_ENCHANTMENT,permanent);
			final MagicTargetChoice targetChoice = new MagicTargetChoice(
	                targetFilter,false,MagicTargetHint.Negative,"another artifact or enchantment to exile");
	        final MagicChoice mayChoice = new MagicMayChoice(player + " may exile another artifact or enchantment.",targetChoice);
			return new MagicEvent(
                    permanent,
                    player,
                    mayChoice,
                    MagicExileTargetPicker.getInstance(),
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
    
    public static final MagicWhenLeavesPlayTrigger T2 = new MagicWhenLeavesPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent data) {
			if (permanent == data &&
				!permanent.getExiledCards().isEmpty()) {
				final MagicCard exiledCard = permanent.getExiledCards().get(0);
				return new MagicEvent(
						permanent,
						permanent.getController(),
						new Object[]{exiledCard},
						this,
						"Return " + exiledCard + " to the battlefield");
			}
            return MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicCard exiledCard = (MagicCard)data[0];
			game.doAction(new MagicRemoveCardAction(exiledCard,MagicLocationType.Exile));
			game.doAction(new MagicPlayCardAction(exiledCard,exiledCard.getOwner(),MagicPlayCardAction.NONE));
		}
    };
}
