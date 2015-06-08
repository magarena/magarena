[
    new MagicPreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.getTarget() == permanent && 
                damage.getSource().hasType(MagicType.Creature) && 
                damage.getSource().hasAbility(MagicAbility.FirstStrike)
                ) {
                    damage.prevent();
                }
            return MagicEvent.NONE;
        }
    }
]
