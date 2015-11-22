[
    new PreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.isTargetCreature() && damage.getTarget().hasSubType(MagicSubType.Cleric) && permanent.isFriend(damage.getTarget())) {
                // Prevention effect.
                damage.prevent(1);
            }
            return MagicEvent.NONE;
        }
    }
]
