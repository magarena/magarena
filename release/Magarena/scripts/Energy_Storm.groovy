[
    new PreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.getSource().hasType(MagicType.Instant) || damage.getSource().hasType(MagicType.Sorcery)) {
                // Prevention effect.
                damage.prevent();
            }
            return MagicEvent.NONE;
        }
    }
]
