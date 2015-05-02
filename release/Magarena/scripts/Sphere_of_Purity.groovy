[
    new MagicPreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (permanent.isController(damage.getTarget()) && damage.getSource().hasType(MagicType.Artifact)) {
                // Prevention effect.
                damage.prevent(1);
            }
            return MagicEvent.NONE;
        }
    }
]
