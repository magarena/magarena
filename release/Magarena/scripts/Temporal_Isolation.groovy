[
    new MagicIfDamageWouldBeDealtTrigger(1) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.getSource() == permanent.getEnchantedCreature()) {
                // Replacement effect. Generates no event or action.
                damage.setAmount(0);
            }
            return MagicEvent.NONE;
        }
    }
]
