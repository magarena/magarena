[
    new MagicPreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (permanent.isController(damage.getTarget()) && damage.getSource().hasColor(MagicColor.Red)) {
                // Prevention effect.
                damage.prevent(2);
            }
            return MagicEvent.NONE;
        }
    }
]
