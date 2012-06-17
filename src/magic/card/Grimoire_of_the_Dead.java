package magic.card;

import java.util.Collection;
import java.util.EnumSet;

import magic.model.MagicCard;
import magic.model.MagicColor;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.action.MagicAddStaticAction;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostTapEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicRemoveCounterEvent;
import magic.model.event.MagicSacrificeEvent;
import magic.model.event.MagicTapEvent;
import magic.model.event.MagicTiming;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

public class Grimoire_of_the_Dead {
	private static final MagicStatic Zombie = new MagicStatic(MagicLayer.Type) {
		@Override
		public void modSubTypeFlags(final MagicPermanent permanent,final EnumSet<MagicSubType> flags) {
			flags.add(MagicSubType.Zombie);
		}
	};

	private static final MagicStatic Black = new MagicStatic(MagicLayer.Color) {
		@Override
		public int getColorFlags(final MagicPermanent permanent,final int flags) {
			return flags|MagicColor.Black.getMask();
		}
	};
	public static final MagicPermanentActivation A1 = new MagicPermanentActivation(
			new MagicCondition[]{
				MagicCondition.CAN_TAP_CONDITION,
                MagicCondition.HAS_CARD_CONDITION,
                MagicManaCost.ONE.getCondition()},
            new MagicActivationHints(MagicTiming.Main,true),
            "Add counter") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
				new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.ONE),
				new MagicDiscardEvent(source,source.getController(),1,false)
			};
		}
		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    "Put a study counter on " + source + ".");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.Charge,1,true));
		}
	};
	
	public static final MagicPermanentActivation A2 = new MagicPermanentActivation(
			new MagicCondition[]{
					MagicCondition.CAN_TAP_CONDITION,
					MagicCondition.THREE_CHARGE_COUNTERS_CONDITION},
            new MagicActivationHints(MagicTiming.Token),
            "Reanimate") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
				new MagicTapEvent((MagicPermanent)source),
				new MagicRemoveCounterEvent((MagicPermanent)source,MagicCounterType.Charge,3),
				new MagicSacrificeEvent((MagicPermanent)source)};
		}
		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player = source.getController();
			return new MagicEvent(
                    source,
                    player,
                    new Object[]{player},
                    this,
                    "Put all creature cards from all graveyards onto the " +
                    "battlefield under your control. They're black Zombies " +
                    "in addition to their other colors and types.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPlayer player = (MagicPlayer)data[0];
			final Collection<MagicTarget> targets =
					game.filterTargets(player,MagicTargetFilter.TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS);
			for (final MagicTarget target : targets) {
				final MagicCard card = (MagicCard) target;
				if (card.getOwner().getGraveyard().contains(card)) {
					final MagicPlayCardAction action = new MagicPlayCardAction(card,player,MagicPlayCardAction.NONE);
					game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
					game.doAction(action);

					final MagicPermanent permanent = action.getPermanent();
					game.doAction(new MagicAddStaticAction(permanent, Zombie));
					game.doAction(new MagicAddStaticAction(permanent, Black));
				}
			}
		}
	};
}
