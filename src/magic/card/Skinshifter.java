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
import magic.model.mstatic.MagicStatic;
import magic.model.mstatic.MagicLayer;

import java.util.EnumSet;

public class Skinshifter {
    // becomes a 4/4 Rhino and gains trample
    private static final MagicStatic PT1 = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.set(4,4);
        }
    };
    private static final MagicStatic AB1 = new MagicStatic(MagicLayer.Ability, MagicStatic.UntilEOT) {
        @Override
        public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
            return flags|MagicAbility.Trample.getMask();
        }
    };
    private static final MagicStatic ST1 = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent,final EnumSet<MagicSubType> flags) {
            flags.removeAll(MagicSubType.ALL_CREATURES);
            flags.add(MagicSubType.Rhino);
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
            game.doAction(new MagicBecomesCreatureAction((MagicPermanent)data[0],PT1,AB1,ST1));
        }
    };
    
    // becomes a 2/2 Bird and gains flying
    private static final MagicStatic PT2 = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.set(2,2);
        }
    };
    private static final MagicStatic AB2 = new MagicStatic(MagicLayer.Ability, MagicStatic.UntilEOT) {
        @Override
        public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
            return flags|MagicAbility.Flying.getMask();
        }
    };
    private static final MagicStatic ST2 = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent,final EnumSet<MagicSubType> flags) {
            flags.removeAll(MagicSubType.ALL_CREATURES);
            flags.add(MagicSubType.Bird);
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
            game.doAction(new MagicBecomesCreatureAction((MagicPermanent)data[0],PT2,AB2,ST2));
        }
    };
    
    // becomes a 0/8 Plant
    private static final MagicStatic PT3 = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.set(0,8);
        }
    };
    private static final MagicStatic ST3 = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent,final EnumSet<MagicSubType> flags) {
            flags.removeAll(MagicSubType.ALL_CREATURES);
            flags.add(MagicSubType.Plant);
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
            game.doAction(new MagicBecomesCreatureAction((MagicPermanent)data[0],PT3,ST3));
        }
    };
}
