[
    new PreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.getSource() == permanent && damage.getTarget().hasColor(MagicColor.Red) && damage.getTarget().hasType(MagicType.Creature)) {
                    damage.prevent();
                }
            return MagicEvent.NONE;
        }
    }
]
