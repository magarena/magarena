[
    new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.PREVENT_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.isCombat() && 
                damage.getTarget().getController == permanent.getController() &&
                damage.getTarget().isAttacking() &&
                damage.getTarget().isCreature()
                ) {
                // Prevention effect.
                damage.prevent();
            }
            return MagicEvent.NONE;
        }
    }
]
