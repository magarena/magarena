[
    new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.REPLACE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (permanent.isController(damage.getTarget()) && permanent.getEnchantedCreature().isValid()) {
                // Replacement effect. Generates no event or action.
                damage.setTarget(permanent.getEnchantedCreature());
            }
            return MagicEvent.NONE;
        }
    }
]
