[
    new PreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (permanent.isController(damage.getTarget()) && damage.getSource().hasType(MagicType.Creature)) {
                damage.prevent(1);
            }
            return MagicEvent.NONE;
        }
    }
]
