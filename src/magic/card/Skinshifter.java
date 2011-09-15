package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.action.MagicBecomesCreatureAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicPlayAbilityEvent;
import magic.model.event.MagicTiming;
import magic.model.variable.MagicDummyLocalVariable;

import java.util.EnumSet;

public class Skinshifter {
	// becomes a 4/4 Rhino and gains trample
    private static final MagicDummyLocalVariable LV1 = new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.power = 4;
			pt.toughness = 4;
		}
		@Override
		public long getGivenAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return flags|MagicAbility.Trample.getMask();
		}
		@Override
		public EnumSet<MagicSubType> getSubTypeFlags(final MagicPermanent permanent,final EnumSet<MagicSubType> flags) {
			return EnumSet.of(MagicSubType.Rhino);
		}
	};

	public static final MagicPermanentActivation A1 = new MagicPermanentActivation(
            new MagicCondition[]{
            		MagicCondition.ABILITY_ONCE_CONDITION,
            		MagicManaCost.GREEN.getCondition()},
            new MagicActivationHints(MagicTiming.Animate,false,1),
            "Rhino") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(
                    source,
                    source.getController(),
                    MagicManaCost.GREEN),
                    new MagicPlayAbilityEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    "Until end of turn, " + source + 
                    " becomes a 4/4 Rhino and gains trample.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicBecomesCreatureAction((MagicPermanent)data[0],LV1));
		}
	};
	
	// becomes a 2/2 Bird and gains flying
    private static final MagicDummyLocalVariable LV2 = new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.power = 2;
			pt.toughness = 2;
		}
		@Override
		public long getGivenAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return flags|MagicAbility.Flying.getMask();
		}
		@Override
		public EnumSet<MagicSubType> getSubTypeFlags(final MagicPermanent permanent,final EnumSet<MagicSubType> flags) {
			return EnumSet.of(MagicSubType.Bird);
		}
	};

	public static final MagicPermanentActivation A2 = new MagicPermanentActivation(
            new MagicCondition[]{
            		MagicCondition.ABILITY_ONCE_CONDITION,
            		MagicManaCost.GREEN.getCondition()},
            new MagicActivationHints(MagicTiming.Animate,false,1),
            "Bird") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(
                    source,
                    source.getController(),
                    MagicManaCost.GREEN),
                    new MagicPlayAbilityEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    "Until end of turn, " + source + 
                    " becomes a 2/2 Bird and gains flying.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicBecomesCreatureAction((MagicPermanent)data[0],LV2));
		}
	};
	
	// becomes a 0/8 Plant
    private static final MagicDummyLocalVariable LV3 = new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.power = 0;
			pt.toughness = 8;
		}
		@Override
		public EnumSet<MagicSubType> getSubTypeFlags(final MagicPermanent permanent,final EnumSet<MagicSubType> flags) {
			return EnumSet.of(MagicSubType.Plant);
		}
	};

	public static final MagicPermanentActivation A3 = new MagicPermanentActivation(
            new MagicCondition[]{
            		MagicCondition.ABILITY_ONCE_CONDITION,
            		MagicManaCost.GREEN.getCondition()},
            new MagicActivationHints(MagicTiming.Animate,false,1),
            "Plant") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(
                    source,
                    source.getController(),
                    MagicManaCost.GREEN),
                    new MagicPlayAbilityEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    "Until end of turn, " + source + 
                    " becomes a becomes a 0/8 Plant.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicBecomesCreatureAction((MagicPermanent)data[0],LV3));
		}
	};
}
