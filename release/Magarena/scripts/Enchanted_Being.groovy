[
    new PreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.isCombat()) {
                final MagicPermanent damageSource = (MagicPermanent)damage.getSource();
                if (permanent == damage.getTarget() && damageSource.isEnchanted() && damageSource.isCreaturePermanent()) {
                    damage.prevent();
                }
            }
            return MagicEvent.NONE;
        }
    }
]
