package magic.model.trigger;

import magic.model.MagicPermanent;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.MagicAbility;
import magic.model.MagicCounterType;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSoulbondEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.action.MagicChangeCountersAction;
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
    
    public static final MagicWhenOtherComesIntoPlayTrigger create(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new MagicWhenOtherComesIntoPlayTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent played) {
                return filter.accept(permanent.getGame(), permanent.getController(), played);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent played) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }

    public static final MagicWhenOtherComesIntoPlayTrigger Evolve = new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isCreature() &&
                    permanent.isCreature() &&
                    permanent.isFriend(otherPermanent) &&
                    (otherPermanent.getPower() > permanent.getPower() ||
                     otherPermanent.getToughness() > permanent.getToughness())) ?
                new MagicEvent(
                    permanent,
                    otherPermanent,
                    this,
                    "PN put a +1/+1 counter on SN.") :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getRefPermanent().getPower() > event.getPermanent().getPower() ||
                event.getRefPermanent().getToughness() > event.getPermanent().getToughness()) {
                    game.doAction(new MagicChangeCountersAction(
                        event.getPermanent(),
                        MagicCounterType.PlusOne,
                        1,
                        true
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
            if (event.isYes()) {
                game.doAction(new MagicChangeCountersAction(
                    event.getPermanent(),
                    MagicCounterType.PlusOne,
                    -1,
                    true
                ));
                game.doAction(new MagicChangeCountersAction(
                    event.getRefPermanent(),
                    MagicCounterType.PlusOne,
                    1,
                    true
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
                     game.filterPermanents(controller,MagicTargetFilterFactory.UNPAIRED_CREATURE_YOU_CONTROL).size() > 1)
                    ||
                    (!hasSoulbond &&
                     game.filterPermanents(controller,MagicTargetFilterFactory.UNPAIRED_SOULBOND_CREATURE).size() > 0)) {
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
