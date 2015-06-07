[
    new MagicPreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.getSource().isFriend(permanent) && damage.getTarget().hasType(MagicType.Creature) && damage.getTarget().isFriend(permanent)) {
                    damage.prevent();
                }
            return MagicEvent.NONE;
        }
    }
]
