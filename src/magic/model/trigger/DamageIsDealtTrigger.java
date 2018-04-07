package magic.model.trigger;

import magic.data.CardDefinitions;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicCounterType;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.action.BecomeMonarchAction;
import magic.model.action.CastCardAction;
import magic.model.action.ChangeCountersAction;
import magic.model.action.ChangeStateAction;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.event.MagicEventFactory;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetType;

public abstract class DamageIsDealtTrigger extends MagicTrigger<MagicDamage> {
    public DamageIsDealtTrigger(final int priority) {
        super(priority);
    }

    public DamageIsDealtTrigger() {
    }

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
        return damage.getDealtAmount() > 0;
    }

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenDamageIsDealt;
    }

    public static DamageIsDealtTrigger DamageToTarget(final MagicTargetFilter<MagicPermanent> filter, final MagicTargetFilter<MagicTarget> tfilter, final MagicEventFactory eventFactory, final boolean isCombat) {
        return new DamageIsDealtTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
                return super.accept(permanent, damage) &&
                    damage.getSource().isPermanent() &&
                    filter.accept(permanent, permanent.getController(), damage.getSourcePermanent()) &&
                    ((damage.isTargetPlayer() && tfilter.acceptType(MagicTargetType.Player)) ||
                     (damage.isTargetCreature() && tfilter.acceptType(MagicTargetType.Permanent))) &&
                    tfilter.accept(permanent, permanent.getController(), damage.getTarget()) &&
                    (!isCombat || damage.isCombat());
            }

            @Override
            public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
                return eventFactory.getTriggerEvent(permanent, damage.getTarget());
            }
        };
    }

    public static DamageIsDealtTrigger DamageToYou(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent, final boolean isCombat) {
        return new DamageIsDealtTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
                return super.accept(permanent, damage) &&
                    damage.getSource().isPermanent() &&
                    filter.accept(permanent, permanent.getController(), damage.getSourcePermanent()) &&
                    permanent.isController(damage.getTarget()) &&
                    (!isCombat || damage.isCombat());
            }

            @Override
            public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
                return sourceEvent.getTriggerEvent(permanent, damage.getSourcePermanent());
            }
        };
    }

    public static DamageIsDealtTrigger DamageToAny(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent, final boolean isCombat) {
        return new DamageIsDealtTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
                return super.accept(permanent, damage) &&
                    damage.getSource().isPermanent() &&
                    filter.accept(permanent, permanent.getController(), damage.getSourcePermanent()) &&
                    (!isCombat || damage.isCombat());
            }

            @Override
            public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
                return sourceEvent.getTriggerEvent(permanent, damage.getSourcePermanent());
            }
        };
    }

    public static DamageIsDealtTrigger DealtDamage(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent, final boolean isCombat) {
        return new DamageIsDealtTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
                return super.accept(permanent, damage) &&
                    damage.isTargetCreature() &&
                    filter.accept(permanent, permanent.getController(), damage.getTargetPermanent()) &&
                    (!isCombat || damage.isCombat());
            }

            @Override
            public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
                return sourceEvent.getTriggerEvent(permanent, damage.getTargetPermanent());
            }
        };
    }

    public static DamageIsDealtTrigger Cipher(final MagicCardDefinition cardDef) {
        return new DamageIsDealtTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
                return super.accept(permanent, damage) && damage.isSource(permanent) && damage.isCombat() && damage.isTargetPlayer();
            }

            @Override
            public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
                return new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    this,
                    "PN may$ cast " + cardDef + " without paying its mana cost"
                );
            }

            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                if (event.isYes()) {
                    game.doAction(CastCardAction.WithoutManaCost(
                        event.getPlayer(),
                        MagicCard.createTokenCard(cardDef, event.getPlayer()),
                        MagicLocationType.Exile,
                        MagicLocationType.Graveyard
                    ));
                }
            }
        };
    }

    public static DamageIsDealtTrigger Poisonous(final int n) {
        return new DamageIsDealtTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
                return super.accept(permanent, damage) && damage.isSource(permanent) && damage.isCombat() && damage.isTargetPlayer();
            }

            @Override
            public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
                return new MagicEvent(
                    permanent,
                    damage.getTarget(),
                    this,
                    n == 1 ?
                        "RN gets a poison counter." :
                        "RN gets " + n + " poison counters."
                );
            }

            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new ChangeCountersAction(event.getRefPlayer(), MagicCounterType.Poison, n));
            }
        };
    }

    public static DamageIsDealtTrigger Renown(final int n) {
        return new DamageIsDealtTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
                return super.accept(permanent, damage) &&
                    damage.isSource(permanent) &&
                    damage.isCombat() &&
                    damage.isTargetPlayer() &&
                    !permanent.hasState(MagicPermanentState.Renowned);
            }

            @Override
            public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
                return new MagicEvent(
                    permanent,
                    this,
                    n == 1 ?
                        "PN puts a +1/+1 counter on SN. SN becomes Renowned" :
                        "PN puts " + n + " +1/+1 counters on SN. SN becomes Renowned"
                );
            }

            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new ChangeCountersAction(event.getPermanent(), MagicCounterType.PlusOne, n));
                game.doAction(ChangeStateAction.Set(event.getPermanent(), MagicPermanentState.Renowned));
            }
        };
    }

    public static DamageIsDealtTrigger Monarch = new DamageIsDealtTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
            return super.accept(permanent, damage) &&
                damage.getSource().isCreaturePermanent() &&
                damage.isCombat() &&
                damage.isTargetPlayer() &&
                damage.getTargetPlayer().isMonarch();
        }

        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            return new MagicEvent(
                game.createDelayedSource(CardDefinitions.getCard("The Monarch"), damage.getTargetPlayer()),
                damage.getTargetPlayer(),
                damage.getSource().getController(),
                this,
                "RN becomes the monarch."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new BecomeMonarchAction(event.getRefPlayer()));
        }
    };
}
