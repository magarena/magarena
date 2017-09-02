[
    new PreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.getTarget() == permanent.getEquippedCreature()) {
                final int amount = CREATURE_YOU_CONTROL.filter(permanent.getController()).size();
                // Prevention effect.
                damage.prevent(amount);
            }
            return MagicEvent.NONE;
        }
    }
]
