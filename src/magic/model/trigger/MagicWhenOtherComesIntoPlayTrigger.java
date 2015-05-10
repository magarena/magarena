package magic.model.trigger;

import magic.model.MagicAbility;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.ChangeCountersAction;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSoulbondEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetFilterFactory;

public abstract class MagicWhenOtherComesIntoPlayTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenOtherComesIntoPlayTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenOtherComesIntoPlayTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenOtherComesIntoPlay;
    }

    public static final MagicWhenOtherComesIntoPlayTrigger createSelfOrAnother(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new MagicWhenOtherComesIntoPlayTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent played) {
                return permanent == played || filter.accept(permanent, permanent.getController(), played);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent played) {
                return sourceEvent.getEvent(permanent, played);
            }
        };
    }
    public static final MagicWhenOtherComesIntoPlayTrigger createAnother(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new MagicWhenOtherComesIntoPlayTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent played) {
                return permanent != played && filter.accept(permanent, permanent.getController(), played);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent played) {
                return sourceEvent.getEvent(permanent, played);
            }
        };
    }
    
    public static final MagicWhenOtherComesIntoPlayTrigger create(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new MagicWhenOtherComesIntoPlayTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent played) {
                return filter.accept(permanent, permanent.getController(), played);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent played) {
                return sourceEvent.getEvent(permanent, played);
            }
        };
    }

    public static final MagicWhenOtherComesIntoPlayTrigger Evolve = new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isCreature() &&
                    permanent.isCreature() &&
                    permanent.isFriend(otherPermanent) &&
                    (otherPermanent.getPowerValue() > permanent.getPowerValue() ||
                     otherPermanent.getToughnessValue() > permanent.getToughnessValue())) ?
                new MagicEvent(
                    permanent,
                    otherPermanent,
                    this,
                    "PN put a +1/+1 counter on SN.") :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getRefPermanent().getPowerValue() > event.getPermanent().getPowerValue() ||
                event.getRefPermanent().getToughnessValue() > event.getPermanent().getToughnessValue()) {
                    game.doAction(new ChangeCountersAction(
                        event.getPermanent(),
                        MagicCounterType.PlusOne,
                        1
                    ));
            }
        }
    };
    
    public static final MagicWhenOtherComesIntoPlayTrigger Graft = new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isCreature() &&
                    otherPermanent != permanent &&
                    permanent.getCounters(MagicCounterType.PlusOne)>0) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice("Move a +1/+1 counter?"),
                    otherPermanent,
                    this,
                    "PN may$ move a +1/+1 counter from SN onto RN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes() && event.getPermanent().hasCounters(MagicCounterType.PlusOne)) {
                game.doAction(new ChangeCountersAction(
                    event.getPermanent(),
                    MagicCounterType.PlusOne,
                    -1
                ));
                game.doAction(new ChangeCountersAction(
                    event.getRefPermanent(),
                    MagicCounterType.PlusOne,
                    1
                ));
            }
        }
    };

    public static final MagicWhenOtherComesIntoPlayTrigger Soulbond = new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent NONE,final MagicPermanent otherPermanent) {
            final MagicPlayer controller = otherPermanent.getController();
            if (otherPermanent.isCreature() &&
                controller.getNrOfPermanents(MagicType.Creature) > 1) {
                final boolean hasSoulbond = otherPermanent.hasAbility(MagicAbility.Soulbond);
                if ((hasSoulbond &&
                     MagicTargetFilterFactory.UNPAIRED_CREATURE_YOU_CONTROL.filter(controller).size() > 1)
                    ||
                    (!hasSoulbond &&
                     MagicTargetFilterFactory.UNPAIRED_SOULBOND_CREATURE.filter(controller).size() > 0)) {
                    return new MagicSoulbondEvent(otherPermanent,hasSoulbond);
                } else {
                    return MagicEvent.NONE;
                }
            } else {
                return MagicEvent.NONE;
            }
        }
    };
}
