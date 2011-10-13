package magic.card;

import magic.model.MagicCard;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicExileUntilThisLeavesPlayAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTapEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicExileTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;

public class Lightning_Crafter {
	public static final MagicWhenComesIntoPlayTrigger T1 = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
			final MagicTargetFilter targetFilter = 
					new MagicTargetFilter.MagicOtherPermanentTargetFilter(
	                MagicTargetFilter.TARGET_GOBLIN_OR_SHAMAN_YOU_CONTROL,permanent);
			final MagicTargetChoice targetChoice = 
					new MagicTargetChoice(
	                targetFilter,false,MagicTargetHint.None,"another Goblin or Shaman to exile");
	        final MagicChoice championChoice = 
	        		new MagicMayChoice(
	        		"You may exile another Goblin or Shaman. " +
	        		"If you don't, sacrifice " + permanent + ".",
	        		targetChoice);
			return new MagicEvent(
                    permanent,
                    player,
                    championChoice,
                    MagicExileTargetPicker.getInstance(),
                    new Object[]{permanent},
                    this,
                    "You may$ exile another Goblin or Shaman$. " +
                    "If you don't, sacrifice " + permanent + ".");
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
					public void doAction(final MagicPermanent creature) {
						game.doAction(new MagicExileUntilThisLeavesPlayAction(permanent,creature));
					}
				});
			} else {
				game.doAction(new MagicSacrificeAction(permanent));
			}
		}
    };
    
    public static final MagicWhenLeavesPlayTrigger T2 = new MagicWhenLeavesPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent data) {
			if (permanent == data &&
				permanent.getExiledCard() != MagicCard.NONE) {
				final MagicCard exiledCard = permanent.getExiledCard();
				return new MagicEvent(
						permanent,
						permanent.getController(),
						new Object[]{exiledCard,permanent.getController()},
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
    
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Removal),
            "Damage") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(3),
                    new Object[]{source},
                    this,
                    source + " deals 3 damage to target creature or player$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
            event.processTarget(game,choiceResults,0,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage = new MagicDamage((MagicSource)data[0],target,3,false);
                    game.doAction(new MagicDealDamageAction(damage));
                }
			});
		}
	};
}
