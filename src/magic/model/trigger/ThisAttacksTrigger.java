package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.event.MagicExertEvent;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.action.EnqueueTriggerAction;
import magic.model.action.ChangeCountersAction;
import magic.model.target.MagicTargetFilterFactory;

public abstract class ThisAttacksTrigger extends AttacksTrigger {
    public ThisAttacksTrigger(final int priority) {
        super(priority);
    }

    public ThisAttacksTrigger() {}

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPermanent attacker) {
        return permanent == attacker;
    }

    public static ThisAttacksTrigger create(final MagicSourceEvent sourceEvent) {
        return new ThisAttacksTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent attacker) {
                return sourceEvent.getTriggerEvent(permanent);
            }
        };
    }

    public static ThisAttacksTrigger exert(final MagicSourceEvent sourceEvent) {
        return new ThisAttacksTrigger(MagicTrigger.REPLACEMENT) {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent attacker) {
                return new MagicEvent(
                    permanent,
                    new MagicMayChoice("Exert " + permanent + "?"),
                    this,
                    "PN may$ exert SN as it attacks."
                );
            }
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                if (event.isYes()) {
                    game.addEvent(new MagicExertEvent(event.getPermanent()));
                    if (sourceEvent != null) {
                        game.doAction(new EnqueueTriggerAction(sourceEvent.getTriggerEvent(event.getPermanent())));
                    }
                }
            }
        };
    }

    public static ThisAttacksTrigger Mentor = new ThisAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent attacker) {
            return new MagicEvent(
                permanent,
                new MagicTargetChoice(
                    MagicTargetFilterFactory.MENTOR,
                    "an attacking creature you control with lesser power than " + permanent
                ),
                this,
                "PN puts a +1/+1 counter on target attacking creature with less power than SN.$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, permanent -> {
                game.doAction(new ChangeCountersAction(event.getPlayer(), permanent, MagicCounterType.PlusOne, 1));
            });
        }
    };

}
