[
    new PreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (permanent.isController(damage.getTarget()) && damage.getSource().hasColor(MagicColor.Blue)) {
                // Prevention effect.
                damage.prevent(2);
            }
            return MagicEvent.NONE;
        }
    }
]
