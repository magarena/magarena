[
    new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.PREVENT_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            final MagicTarget target = damage.getTarget();
			if (target != permanent && 
                target.isCreature() &&
                permanent.isFriend(target) &&
				target.getCardDefinition().isToken()) {
                damage.prevent();
            }
            return MagicEvent.NONE;
        }
    }
]
