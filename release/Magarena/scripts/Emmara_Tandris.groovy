[
    new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.PREVENT_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            final MagicTarget target = damage.getTarget();
            if (target.isCreature() &&
                target.isToken() &&
                target.isFriend(permanent)) {
                damage.prevent();
            }
            return MagicEvent.NONE;
        }
    }
]
