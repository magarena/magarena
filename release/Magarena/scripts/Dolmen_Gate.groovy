[
    new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.PREVENT_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.isCombat() && damage.isTargetCreature()) {
                final MagicPermanent creature = damage.getTargetPermanent();
                if (creature.isAttacking() && creature.isFriend(permanent)) {
                    // Prevention effect.
                    damage.prevent();
                }
            }
            return MagicEvent.NONE;
        }
    }
]
