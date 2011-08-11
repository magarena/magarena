package magic.card;

import magic.model.*;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicDealDamageAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Shrine_of_Burning_Rage {

	public static final MagicPermanentActivation A1 = new MagicPermanentActivation(
            new MagicCondition[]{
                MagicCondition.CAN_TAP_CONDITION,
                MagicManaCost.THREE.getCondition()},
            new MagicActivationHints(MagicTiming.Removal),
            "Damage") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return new MagicEvent[]{
				new MagicTapEvent(permanent),
				new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.THREE),
				new MagicSacrificeEvent(permanent)};
		}

		@Override
		public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(source.getCounters(MagicCounterType.Charge)),
                    new Object[]{source},
                    this,
                    "Shrine of Burning Rage deals damage equal to the number of charge counters on it to target creature or player$.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicPermanent source=(MagicPermanent)data[0];
				final int amount=source.getCounters(MagicCounterType.Charge);
				if (amount>0) {
					final MagicDamage damage=new MagicDamage(source,target,amount,false);
					game.doAction(new MagicDealDamageAction(damage));
				}
			}
		}
	};
	
    public static final MagicTrigger T1 = new MagicTrigger(MagicTriggerType.AtUpkeep) {
		@Override
		public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final Object data) {
			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent},
                        this,
                        "Put a charge counter on Shrine of Burning Rage.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicChangeCountersAction(
                        (MagicPermanent)data[0],
                        MagicCounterType.Charge,
                        1,
                        true));
		}		
    };
    
    public static final MagicTrigger T2 = new MagicTrigger(MagicTriggerType.WhenSpellIsPlayed) {
		@Override
		public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final Object data) {
			final MagicPlayer player=permanent.getController();
			final MagicCard card=((MagicCardOnStack)data).getCard();
			if (card.getOwner()==player&&MagicColor.Red.hasColor(card.getColorFlags())) {
				return new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent},
                        this,
                        "Put a charge counter on Shrine of Burning Rage.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicChangeCountersAction(
                        (MagicPermanent)data[0],
                        MagicCounterType.Charge,
                        1,
                        true));
		}		
    };
}
