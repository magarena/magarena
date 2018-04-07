package magic.model.trigger;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetType;
import magic.model.action.PreventDamageAction;

public abstract class PreventDamageTrigger extends IfDamageWouldBeDealtTrigger {
    public PreventDamageTrigger() {
        super(MagicTrigger.PREVENT_DAMAGE);
    }

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
        return damage.getAmount() > 0 && !damage.isUnpreventable();
    }

    public static final PreventDamageTrigger ProtectionShield = new PreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicTarget target = damage.getTarget();

            // Protection from source prevents all damage
            if (target.isPermanent() && damage.getTargetPermanent().hasProtectionFrom(damage.getSource())) {
                damage.prevent();
            }

            return MagicEvent.NONE;
        }
    };

    public static final PreventDamageTrigger PreventDamageShield = new PreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicTarget target = damage.getTarget();

            // Prevent x amount of damage
            final int reduction = Math.min(damage.getAmount(), target.getPreventDamage());
            if (reduction > 0) {
                damage.prevent(reduction);
                game.doAction(new PreventDamageAction(target, -reduction));
            }

            return MagicEvent.NONE;
        }
    };

    public static final PreventDamageTrigger PreventCombatDamageDealtToDealtBy = new PreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.isCombat() && (damage.isTarget(permanent) || damage.isSource(permanent))) {
                damage.prevent();
            }
            return MagicEvent.NONE;
        }
    };

    public static final PreventDamageTrigger PreventCombatDamageDealtTo = new PreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.isCombat() && damage.isTarget(permanent)) {
                damage.prevent();
            }
            return MagicEvent.NONE;
        }
    };

    public static final PreventDamageTrigger PreventCombatDamageDealtBy = new PreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.isCombat() && damage.isSource(permanent)) {
                damage.prevent();
            }
            return MagicEvent.NONE;
        }
    };

    public static final PreventDamageTrigger PreventDamageDealtTo = new PreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.isTarget(permanent)) {
                damage.prevent();
            }
            return MagicEvent.NONE;
        }
    };

    public static final PreventDamageTrigger PreventDamageDealtBy = new PreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.isSource(permanent)) {
                damage.prevent();
            }
            return MagicEvent.NONE;
        }
    };

    public static final PreventDamageTrigger PreventCombatDamage = new PreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.isCombat()) {
                damage.prevent();
            }
            return MagicEvent.NONE;
        }
    };

    // prevent all damage that would be dealt to you or creatures you control this turn
    public static final PreventDamageTrigger PreventDamageDealtToYouOrCreaturesYouControl(final MagicPlayer player) {
        return new PreventDamageTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
                if (damage.getTarget().getController().getId() == player.getId()) {
                    damage.prevent();
                }
                return MagicEvent.NONE;
            }
        };
    }

    // prevent all combat damage that would be dealt to you or creatures you control this turn
    public static final PreventDamageTrigger PreventCombatDamageDealtToYouOrCreaturesYouControl(final MagicPlayer player) {
        return new PreventDamageTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
                if (damage.isCombat() &&
                    damage.getTarget().getController().getId() == player.getId()) {
                    damage.prevent();
                }
                return MagicEvent.NONE;
            }
        };
    }

    // prevent all damage that would be dealt to you
    public static final PreventDamageTrigger PreventDamageDealtToYou(final MagicPlayer player) {
        return new PreventDamageTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
                if (damage.getTarget().isPlayer() &&
                    damage.getTarget().getController().getId() == player.getId()) {
                    damage.prevent();
                }
                return MagicEvent.NONE;
            }
        };
    }

    // prevent all combat damage that would be dealt to you
    public static final PreventDamageTrigger PreventCombatDamageDealtToYou(final MagicPlayer player) {
        return new PreventDamageTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
                if (damage.isCombat() &&
                    damage.getTarget().isPlayer() &&
                    damage.getTarget().getController().getId() == player.getId()) {
                    damage.prevent();
                }
                return MagicEvent.NONE;
            }
        };
    }

    // prevent all damage that would be dealt to [permanent]
    public static final PreventDamageTrigger PreventDamageDealtTo(final MagicTargetFilter<MagicTarget> filter) {
        return new PreventDamageTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
                if ((damage.isTargetCreature() &&
                     filter.acceptType(MagicTargetType.Permanent) &&
                     filter.accept(permanent, permanent.getController(), damage.getTargetPermanent())) ||
                    (damage.isTargetPlayer() &&
                     filter.acceptType(MagicTargetType.Player) &&
                     filter.accept(permanent, permanent.getController(), damage.getTargetPlayer()))) {

                    damage.prevent();
                }
                return MagicEvent.NONE;
            }
        };
    }

    // prevent all combat damage that would be dealt to [permanent]
    public static final PreventDamageTrigger PreventCombatDamageDealtTo(final MagicTargetFilter<MagicTarget> filter) {
        return new PreventDamageTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
                if ((damage.isTargetCreature() &&
                     filter.acceptType(MagicTargetType.Permanent) &&
                     filter.accept(permanent, permanent.getController(), damage.getTargetPermanent())) ||
                    (damage.isTargetPlayer() &&
                     filter.acceptType(MagicTargetType.Player) &&
                     filter.accept(permanent, permanent.getController(), damage.getTargetPlayer()))) {

                    if (damage.isCombat()) {
                        damage.prevent();
                    }
                }
                return MagicEvent.NONE;
            }
        };
    }

    // prevent all noncombat damage that would be dealt to [permanent]
    public static final PreventDamageTrigger PreventNonCombatDamageDealtTo(final MagicTargetFilter<MagicTarget> filter) {
        return new PreventDamageTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
                if ((damage.isTargetCreature() &&
                     filter.acceptType(MagicTargetType.Permanent) &&
                     filter.accept(permanent, permanent.getController(), damage.getTargetPermanent())) ||
                    (damage.isTargetPlayer() &&
                     filter.acceptType(MagicTargetType.Player) &&
                     filter.accept(permanent, permanent.getController(), damage.getTargetPlayer()))) {

                    if (!damage.isCombat()) {
                        damage.prevent();
                    }
                }
                return MagicEvent.NONE;
            }
        };
    }

    // prevent all combat damage dealt by
    public static final PreventDamageTrigger PreventCombatDamageDealtBy(final MagicTargetFilter<MagicPermanent> filter) {
        return new PreventDamageTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
                if (damage.isCombat() &&
                    damage.isSourcePermanent() &&
                    filter.accept(permanent, permanent.getController(), damage.getSourcePermanent())) {
                    damage.prevent();
                }
                return MagicEvent.NONE;
            }
        };
    }

    // prevent all damage dealt by
    public static final PreventDamageTrigger PreventDamageDealtBy(final MagicTargetFilter<MagicPermanent> filter) {
        return new PreventDamageTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
                if (damage.isSourcePermanent() &&
                    filter.accept(permanent, permanent.getController(), damage.getSourcePermanent())) {
                    damage.prevent();
                }
                return MagicEvent.NONE;
            }
        };
    }
}
