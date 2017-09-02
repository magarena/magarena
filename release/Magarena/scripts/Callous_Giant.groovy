[
    new PreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final int amount = damage.getAmount();
            if (damage.getTarget() == permanent && amount <= 3) {
                // Prevention effect.
                damage.prevent(amount);
            }
            return MagicEvent.NONE;
        }
    }
]
