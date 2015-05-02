package magic.model.trigger;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.action.PreventDamageAction;

public abstract class MagicPreventDamageTrigger extends MagicIfDamageWouldBeDealtTrigger {
    public MagicPreventDamageTrigger() {
        super(MagicTrigger.PREVENT_DAMAGE);
    }
    
    @Override
    public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
        return damage.getAmount() > 0 && damage.isUnpreventable() == false;
    }
    
    public static final MagicPreventDamageTrigger GlobalPreventDamageToTarget = new MagicPreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicTarget target = damage.getTarget();
           
            // Protection from source reduces damage to 0
            if (target.isPermanent()) {
                final MagicPermanent targetPermanent = (MagicPermanent)target;
                if (targetPermanent.hasProtectionFrom(damage.getSource())) {
                    damage.setAmount(0);
                }
            }

            // Prevent x amount of damage
            final int reduction = Math.min(damage.getAmount(), target.getPreventDamage());
            if (reduction > 0) {
                damage.setAmount(damage.getAmount() - reduction);
                game.doAction(new PreventDamageAction(target, -reduction));
            }

            return MagicEvent.NONE;
        }
    };
    
    public static final MagicPreventDamageTrigger PreventCombatDamageDealtToDealtBy = new MagicPreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.isCombat() && (damage.isTarget(permanent) || damage.isSource(permanent))) {
                damage.prevent();
            }
            return MagicEvent.NONE;
        }
    };
    
    public static final MagicPreventDamageTrigger PreventCombatDamageDealtTo = new MagicPreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.isCombat() && damage.isTarget(permanent)) {
                damage.prevent();
            }
            return MagicEvent.NONE;
        }
    };
    
    public static final MagicPreventDamageTrigger PreventCombatDamageDealtBy = new MagicPreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.isCombat() && damage.isSource(permanent)) {
                damage.prevent();
            }
            return MagicEvent.NONE;
        }
    };
    
    public static final MagicPreventDamageTrigger PreventDamageDealtTo = new MagicPreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.isTarget(permanent)) {
                damage.prevent();
            }
            return MagicEvent.NONE;
        }
    };
    
    public static final MagicPreventDamageTrigger PreventCombatDamage = new MagicPreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.isCombat()) {
                damage.prevent();
            }
            return MagicEvent.NONE;
        }
    };
    
    // prevent all damage that would be dealt to you or creatures you control this turn
    public static final MagicPreventDamageTrigger PreventDamageDealtToYouOrCreaturesYouControl(final MagicPlayer player) {
        return new MagicPreventDamageTrigger() {
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
    public static final MagicPreventDamageTrigger PreventCombatDamageDealtToYouOrCreaturesYouControl(final MagicPlayer player) {
        return new MagicPreventDamageTrigger() {
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
    public static final MagicPreventDamageTrigger PreventDamageDealtToYou(final MagicPlayer player) {
        return new MagicPreventDamageTrigger() {
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
    public static final MagicPreventDamageTrigger PreventCombatDamageDealtToYou(final MagicPlayer player) {
        return new MagicPreventDamageTrigger() {
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
    public static final MagicPreventDamageTrigger PreventDamageDealtTo(final MagicTargetFilter<MagicPermanent> filter) {
        return new MagicPreventDamageTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
                if (damage.getTarget().isPermanent() &&
                    filter.accept(permanent, permanent.getController(), damage.getTargetPermanent())) {
                    damage.prevent();
                }
                return MagicEvent.NONE;
            }
        };
    }
    
    // prevent all combat damage that would be dealt to [permanent]
    public static final MagicPreventDamageTrigger PreventCombatDamageDealtTo(final MagicTargetFilter<MagicPermanent> filter) {
        return new MagicPreventDamageTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
                if (damage.isCombat() &&
                    damage.getTarget().isPermanent() &&
                    filter.accept(permanent, permanent.getController(), damage.getTargetPermanent())) {
                    damage.prevent();
                }
                return MagicEvent.NONE;
            }
        };
    }
}
