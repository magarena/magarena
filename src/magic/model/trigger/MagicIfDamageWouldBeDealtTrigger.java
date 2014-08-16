package magic.model.trigger;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;

public abstract class MagicIfDamageWouldBeDealtTrigger extends MagicTrigger<MagicDamage> {
    public MagicIfDamageWouldBeDealtTrigger(final int priority) {
        super(priority);
    }

    public MagicIfDamageWouldBeDealtTrigger() {}
    
    public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
        return damage.getAmount() > 0;
    }

    public MagicTriggerType getType() {
        return MagicTriggerType.IfDamageWouldBeDealt;
    }
    
    public static final MagicIfDamageWouldBeDealtTrigger PreventCombatDamageDealtToDealtBy = new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.PREVENT_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.isCombat() && (damage.isTarget(permanent) || damage.isSource(permanent))) {
                damage.prevent();
            }
            return MagicEvent.NONE;
        }
    };
    
    public static final MagicIfDamageWouldBeDealtTrigger PreventCombatDamageDealtTo = new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.PREVENT_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.isCombat() && damage.isTarget(permanent)) {
                damage.prevent();
            }
            return MagicEvent.NONE;
        }
    };
    
    public static final MagicIfDamageWouldBeDealtTrigger PreventCombatDamageDealtBy = new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.PREVENT_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.isCombat() && damage.isSource(permanent)) {
                damage.prevent();
            }
            return MagicEvent.NONE;
        }
    };
    
    public static final MagicIfDamageWouldBeDealtTrigger PreventDamageDealtTo = new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.PREVENT_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.isTarget(permanent)) {
                damage.prevent();
            }
            return MagicEvent.NONE;
        }
    };
    
    public static final MagicIfDamageWouldBeDealtTrigger PreventCombatDamage = new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.PREVENT_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.isCombat()) {
                damage.prevent();
            }
            return MagicEvent.NONE;
        }
    };
    
    // prevent all damage that would be dealt to you or creatures you control this turn
    public static final MagicIfDamageWouldBeDealtTrigger PreventDamageDealtToYouOrCreaturesYouControl(final MagicPlayer player) {
        return new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.PREVENT_DAMAGE) {
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
    public static final MagicIfDamageWouldBeDealtTrigger PreventCombatDamageDealtToYouOrCreaturesYouControl(final MagicPlayer player) {
        return new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.PREVENT_DAMAGE) {
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
    public static final MagicIfDamageWouldBeDealtTrigger PreventDamageDealtToYou(final MagicPlayer player) {
        return new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.PREVENT_DAMAGE) {
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
    public static final MagicIfDamageWouldBeDealtTrigger PreventCombatDamageDealtToYou(final MagicPlayer player) {
        return new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.PREVENT_DAMAGE) {
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
}
