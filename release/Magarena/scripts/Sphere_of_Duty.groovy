[
    new PreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (permanent.isController(damage.getTarget()) && damage.getSource().hasColor(MagicColor.Green)) {
                // Prevention effect.
                damage.prevent(2);
            }
            return MagicEvent.NONE;
        }
    }
]
