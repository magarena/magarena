[
    new MagicPreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (permanent.isController(damage.getTarget()) &&
                damage.isCombat() &&
                damage.getSource().isCreature() &&
                permanent.isUntapped()
            ) {
                // Prevention effect.
                damage.prevent(1);
            }
            return MagicEvent.NONE;
        }
    }
]
