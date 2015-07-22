package magic.model.trigger;

import magic.model.MagicCardDefinition;
import magic.model.MagicCounterType;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.action.CastFreeCopyAction;
import magic.model.action.ChangeCountersAction;
import magic.model.action.ChangePoisonAction;
import magic.model.action.ChangeStateAction;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.target.MagicTargetFilter;

public abstract class MagicWhenDamageIsDealtTrigger extends MagicTrigger<MagicDamage> {
    public MagicWhenDamageIsDealtTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenDamageIsDealtTrigger() {
    }

    public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
        return damage.getDealtAmount() > 0;
    }

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenDamageIsDealt;
    }

    public static MagicWhenDamageIsDealtTrigger DamageToCreature(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent, final boolean isCombat) {
        return new MagicWhenDamageIsDealtTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
                return super.accept(permanent, damage) &&
                    damage.getSource().isPermanent() &&
                    filter.accept(permanent, permanent.getController(), damage.getSourcePermanent()) &&
                    damage.isTargetCreature() &&
                    (isCombat == false || damage.isCombat());
            }

            @Override
            public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
                return sourceEvent.getEvent(permanent, damage.getTargetPermanent());
            }
        };
    }

    public static MagicWhenDamageIsDealtTrigger DamageToPlayer(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent, final boolean isCombat) {
        return new MagicWhenDamageIsDealtTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
                return super.accept(permanent, damage) &&
                    damage.getSource().isPermanent() &&
                    filter.accept(permanent, permanent.getController(), damage.getSourcePermanent()) &&
                    damage.isTargetPlayer() &&
                    (isCombat == false || damage.isCombat());
            }

            @Override
            public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
                return sourceEvent.getEvent(permanent, damage.getTargetPlayer());
            }
        };
    }

    public static MagicWhenDamageIsDealtTrigger DamageToYou(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent, final boolean isCombat) {
        return new MagicWhenDamageIsDealtTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
                return super.accept(permanent, damage) &&
                    damage.getSource().isPermanent() &&
                    filter.accept(permanent, permanent.getController(), damage.getSourcePermanent()) &&
                    permanent.isController(damage.getTarget()) &&
                    (isCombat == false || damage.isCombat());
            }

            @Override
            public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
                return sourceEvent.getEvent(permanent, damage.getSourcePermanent());
            }
        };
    }

    public static MagicWhenDamageIsDealtTrigger DamageToOpponent(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent, final boolean isCombat) {
        return new MagicWhenDamageIsDealtTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
                return super.accept(permanent, damage) &&
                    damage.getSource().isPermanent() &&
                    filter.accept(permanent, permanent.getController(), damage.getSourcePermanent()) &&
                    permanent.isOpponent(damage.getTarget()) &&
                    (isCombat == false || damage.isCombat());
            }

            @Override
            public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
                return sourceEvent.getEvent(permanent, damage.getTargetPlayer());
            }
        };
    }

    public static MagicWhenDamageIsDealtTrigger DamageToAny(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent, final boolean isCombat) {
        return new MagicWhenDamageIsDealtTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
                return super.accept(permanent, damage) &&
                    damage.getSource().isPermanent() &&
                    filter.accept(permanent, permanent.getController(), damage.getSourcePermanent()) &&
                    (isCombat == false || damage.isCombat());
            }

            @Override
            public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
                return sourceEvent.getEvent(permanent, damage.getSourcePermanent());
            }
        };
    }

    public static MagicWhenDamageIsDealtTrigger DealtDamage(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent, final boolean isCombat) {
        return new MagicWhenDamageIsDealtTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
                return super.accept(permanent, damage) &&
                    damage.isTargetCreature() &&
                    filter.accept(permanent, permanent.getController(), damage.getTargetPermanent()) &&
                    (isCombat == false || damage.isCombat());
            }

            @Override
            public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
                return sourceEvent.getEvent(permanent, damage.getTargetPermanent());
            }
        };
    }

    public static MagicWhenDamageIsDealtTrigger Cipher(final MagicCardDefinition cardDef) {
        return new MagicWhenDamageIsDealtTrigger() {
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
                    game.doAction(new CastFreeCopyAction(event.getPlayer(), cardDef));
                }
            }
        };
    }

    public static MagicWhenDamageIsDealtTrigger Poisonous(final int n) {
        return new MagicWhenDamageIsDealtTrigger() {
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
                game.doAction(new ChangePoisonAction(event.getRefPlayer(), n));
            }
        };
    }

    public static MagicWhenDamageIsDealtTrigger Renown(final int n) {
        return new MagicWhenDamageIsDealtTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
                return super.accept(permanent, damage) &&
                    damage.isSource(permanent) &&
                    damage.isCombat() &&
                    damage.isTargetPlayer() &&
                    permanent.hasState(MagicPermanentState.Renowned) == false;
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
}
